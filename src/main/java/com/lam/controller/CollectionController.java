package com.lam.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.lam.Handler.GlobalException;
import com.lam.model.entity.dto.resp.Response;
import com.lam.model.entity.po.CollectionBlog;
import com.lam.model.mapper.CollectionMapper;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@Api("博客收藏")
@RestController
@RequestMapping("/Collection")
public class CollectionController {

    public static final String BLOG_KEY="BlogCollection_uid_";

    @Autowired
    CollectionMapper collectionMapper;

    @Autowired
    RedisTemplate redisTemplate;

    @PostMapping("/CollectionBlog")
    @ApiOperation("收藏文章")
    @HystrixCommand(fallbackMethod = "hystrixGet")
    public Response<Void> CollectionBlog(@RequestParam Long blogId,@RequestAttribute Long uId){
        try{
            String key=BLOG_KEY+uId;
            Double isSucessful=redisTemplate.opsForZSet().score(key,blogId);
            if(isSucessful ==null){
                QueryWrapper<CollectionBlog> queryWrapper=new QueryWrapper<>();
                queryWrapper.eq("useId ",uId);
                queryWrapper.eq("blogId",blogId);
                if(collectionMapper.selectOne(queryWrapper)==null){
                    CollectionBlog collectionBlog=new CollectionBlog();
                    collectionBlog.setBlogId(blogId);
                    collectionBlog.setUseId(uId);
                    collectionMapper.insert(collectionBlog);
                }
                else{
                    UpdateWrapper<CollectionBlog> updateWrapper=new UpdateWrapper<>();
                    updateWrapper.eq("useId",uId);
                    updateWrapper.eq("blogId",blogId);
                    updateWrapper.set("isDelete",0);
                    collectionMapper.update(null,updateWrapper);
                }
                redisTemplate.opsForZSet().add(key,blogId,System.currentTimeMillis());

            }
            else{
                UpdateWrapper<CollectionBlog> updateWrapper=new UpdateWrapper<>();
                updateWrapper.eq("useId",uId);
                updateWrapper.eq("blogId",blogId);
                updateWrapper.set("isDelete",1);
                collectionMapper.update(null,updateWrapper);
                redisTemplate.opsForZSet().remove(key,blogId);
            }

            return Response.SUCCESS(null);
        }
        catch (Exception e){
            throw new GlobalException("查找数据库出错");
        }

    }


    public String hystrixGet(){
        return "系统繁忙，请稍后再试";
    }




}
