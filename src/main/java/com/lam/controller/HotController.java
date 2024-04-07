package com.lam.controller;

import com.lam.Handler.GlobalException;
import com.lam.common.ResultCode;
import com.lam.model.entity.dto.resp.Response;
import com.lam.model.entity.po.Blog;
import com.lam.service.impl.BlogServiceImpl;
import com.lam.service.impl.SimpleRedisLock;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/hot")
@Api("点赞和热门数据排行榜")
public class HotController {
    public static final String BLOG_LIKE_KEY="Lampe_Blog_Like_";

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    BlogServiceImpl blogService;

    @ApiOperation("对文章进行点赞")
    @PostMapping("/Like")
    public Response<Void> Like(@RequestParam Long blogId, @RequestAttribute Long uid){
        String key=BLOG_LIKE_KEY+blogId;
        Double score=redisTemplate.opsForZSet().score(key,uid);
        if(score ==null){
            boolean isSucessful=blogService.addLike(blogId);
            if(isSucessful){
                redisTemplate.opsForZSet().add(key,uid,System.currentTimeMillis());
            }

        }
        else{
            boolean isSucessful=blogService.deleteLike(blogId);
            if(isSucessful){
                redisTemplate.opsForZSet().remove(key,uid);
            }
        }
        return Response.SUCCESS(null);
    }


    @ApiOperation("热门排行榜")
    @PostMapping("/hot")
    @HystrixCommand(fallbackMethod = "hystrixGet")
    public Response<List<Blog>> hot(){
        String key="TopCharts";
        boolean isHaving=redisTemplate.opsForValue().getOperations().hasKey(key);
         if(!isHaving){
             SimpleRedisLock simpleRedisLock=new SimpleRedisLock("blog_");
             if(simpleRedisLock.tryLock(1200)){
            List<Blog> HotBlog=blogService.getHot();
            int i=1;
            if(HotBlog!=null){
                for (Blog blog:HotBlog) {
                    redisTemplate.opsForZSet().add(key,blog,i);
                    i++;
                }
            }
            simpleRedisLock.unLock();
            return Response.SUCCESS(HotBlog);
             }else {
                 throw new GlobalException(-1,"系统繁忙，请稍后再试");
             }

         }
         else{
          Set<Blog> blogSet=   redisTemplate.opsForZSet().rangeWithScores(key,0,-1);
          return Response.SUCCESS(new ArrayList<>(blogSet));
         }
    }

    //每天的24点就会将热门榜删掉，但如果一天都没人访问热门榜可能会报错，造成定时任务失效
    @Scheduled(cron = "* * 0 * * *")
    public void removeHot(){
    redisTemplate.opsForValue().getOperations().delete("TopCharts");
    }


    public String hystrixGet(){
        return "系统繁忙，请稍后再试";
    }

}
