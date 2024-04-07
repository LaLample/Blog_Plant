package com.lam.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.lam.Handler.GlobalException;
import com.lam.common.ResultCode;
import com.lam.model.entity.dto.resp.Response;
import com.lam.model.entity.po.Blog;
import com.lam.model.mapper.BlogMapper;
import com.lam.utils.SnowflakeIdWorker;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
//import org.redisson.Redisson;
//import org.redisson.api.RBloomFilter;
//import org.redisson.api.RedissonClient;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Api("文章")
@RestController
@RequestMapping("/Blog")
public class UpdateBlogController {
    public static final String BLOG_KEY="Blog_";
    @Autowired
    BlogMapper blogMapper;

    @Autowired
    RedisTemplate redisTemplate;

    @Resource
    private RedissonClient redissonClient;

    SnowflakeIdWorker snowflakeIdWorker=new SnowflakeIdWorker();



    @ApiOperation("更新文章")
    @PostMapping("/update")
    public Response<Void> update(@RequestBody Blog blog){
        String key=BLOG_KEY+blog;
        UpdateWrapper<Blog> updateWrapper=new UpdateWrapper<>();
        updateWrapper.eq("txtId",blog.getTxtId());
        updateWrapper.eq("isDelete",0);
        updateWrapper.set("Title",blog.getTitle());
        updateWrapper.set("Detail",blog.getDetail());
        blogMapper.update(null,updateWrapper);
        redisTemplate.opsForValue().getOperations().delete(key);
        return Response.SUCCESS(null);
    }

    @ApiOperation("查看文章")
    @PostMapping("/check")
    public Response<Blog> check(@RequestParam Long blogId){
        String key=BLOG_KEY+blogId;
        RBloomFilter<Long> bloomFilter=redissonClient.getBloomFilter("Blog_BloomFilter");
        if(bloomFilter.contains(blogId)){
            boolean isSucessful=redisTemplate.opsForValue().getOperations().hasKey(key);
            if(!isSucessful){
                QueryWrapper<Blog> queryWrapper=new QueryWrapper<>();
                queryWrapper.eq("isDelete",0);
                queryWrapper.eq("txtId",blogId);
                Blog blog=blogMapper.selectOne(queryWrapper);
                redisTemplate.opsForValue().set(key,blog);
                return Response.SUCCESS(blog);

            }
            else{
                Blog blog= (Blog) redisTemplate.opsForValue().get(key);
                return Response.SUCCESS(blog);
            }
        }else{
            throw new GlobalException(-6,"没有查找到对应对象");
        }
    }

//    @ApiOperation("添加文章")
//    @PostMapping("/addBlog")
//    public Response<Void> addBlog(@RequestBody Blog blog,@RequestParam Long uid){
//       Blog newBlog=new Blog();
//        BeanUtils.copyProperties(blog,newBlog);
//        newBlog.setTxtId(snowflakeIdWorker.nextId());
//        newBlog.setUid(uid);
//        String key=BLOG_KEY+newBlog.getTxtId();
//      int i=blogMapper.insert(newBlog);
//        if(i==-1){
//            return Response.FAIL(ResultCode.FAIL);
//        }
//        redisTemplate.opsForValue().set(key,newBlog);
//        //获取一个名为Blog_BloomFilter的布隆过滤器，如果没有则往redis创建一个String类型的
//        RBloomFilter<Long> bloomFilter=redissonClient.getBloomFilter("Blog_BloomFilter");
//        //如果该过滤器是第一次使用则进行初始化
//        bloomFilter.tryInit(60L,0.03);
//        bloomFilter.add(newBlog.getTxtId());
//        redissonClient.shutdown();
//        return Response.SUCCESS(null);
//    }


}
