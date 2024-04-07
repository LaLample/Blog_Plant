package com.lam.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lam.common.ResultCode;
import com.lam.config.RedisUtil;
import com.lam.model.entity.dto.resp.Response;
import com.lam.model.entity.po.Blog;
import com.lam.model.entity.po.Tb_flower;
import com.lam.model.mapper.BlogMapper;
import com.lam.model.mapper.Tb_flowerMapper;
import com.lam.service.MQBlogService;
import com.lam.service.impl.BlogServiceImpl;
import com.lam.service.impl.RedundancyServiceImpl;
import com.lam.utils.SnowflakeIdWorker;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/push")
@Api("推送/获取")
public class PushController {
public static final String FEED_KEY="Lamped_Feed_";
    @Autowired
    BlogServiceImpl blogService;

    @Autowired
    Tb_flowerMapper tb;

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    BlogMapper blogMapper;

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    RedundancyServiceImpl redundancyService;

    @Resource
    private RedissonClient redissonClient;

    @Autowired
    private MQBlogService mq;

    SnowflakeIdWorker snowflakeIdWorker=new SnowflakeIdWorker();
    @PostMapping("/pushArticle")
    @ApiOperation("推送文章")
    public Response<Void> pushArticle(@RequestBody Blog blog, @RequestParam(value = "uid")Long uid){

        blog.setUid(uid);
        Long blogId= snowflakeIdWorker.nextId();
        blog.setTxtId(blogId);
        boolean isTrue=blogService.saveBlog(blog);
        if(!isTrue){
            return Response.FAIL(ResultCode.FAIL);
        }
        String key1=UpdateBlogController.BLOG_KEY+blogId;
        redisTemplate.opsForValue().set(key1,blog);
        //获取一个名为Blog_BloomFilter的布隆过滤器，如果没有则往redis创建一个String类型的
        RBloomFilter<Long> bloomFilter=redissonClient.getBloomFilter("Blog_BloomFilter");
        //如果该过滤器是第一次使用则进行初始化
        bloomFilter.tryInit(60L,0.03);
        bloomFilter.add(blogId);
        redissonClient.shutdown();

        QueryWrapper<Tb_flower> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("isDelete",0);
        queryWrapper.eq("idolId",uid);
        List<Tb_flower> flower=tb.selectList(queryWrapper);
        if(flower!=null){
            for (Tb_flower flo:flower) {
                Long flowerId=flo.getFansId();
                String key=FEED_KEY+flowerId;
                redisUtil.zSSet(key,blog.getTxtId(),System.currentTimeMillis());
            }
        }
        //将数据冗余一份来实现分布式事务可靠性
        redundancyService.RedundancyMessage(blog);
        //将消息投递到消息队列中，实现生产者的可靠性
        mq.sendMessage(blog);
        return Response.SUCCESS(null);

    }

//    @PostMapping("/test")
//    public Response<Void> test(){
//        System.out.println("111");
//        return Response.SUCCESS(null);
//    }
    @PostMapping("/getArticle")
    @ApiOperation(("我的关注"))
    public Response<List<Blog>> getArticle(@RequestAttribute(value = "uid")Long uid){
      String key=FEED_KEY+uid;
       Set<Long> blogSet= redisTemplate.opsForZSet().reverseRange(key,0,-1);
     List<Blog> blogs=blogSet.stream().map(item->{
         Blog blog=new Blog();
         QueryWrapper<Blog> queryWrapper=new QueryWrapper<>();
         queryWrapper.eq("isDelete",0);
         queryWrapper.eq("txtId",item);
         return blogMapper.selectOne(queryWrapper);
     }).collect(Collectors.toList());
    return Response.SUCCESS(blogs);
    }





}
