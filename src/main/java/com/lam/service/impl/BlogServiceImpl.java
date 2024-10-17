package com.lam.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lam.model.entity.po.Blog;
import com.lam.model.mapper.BlogMapper;
import com.lam.service.IBlogService;
import com.lam.utils.SnowflakeIdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog> implements IBlogService {

    SnowflakeIdWorker snowflakeIdWorker=new SnowflakeIdWorker();

    @Autowired
    BlogMapper blogMapper;

    @Async("blogExecutor")
    public boolean saveBlog(Blog blog){
        int i= blogMapper.insert(blog);
        if(i!=-1){
            return true;
        }
        else return false;
    }

    public boolean addLike(Long blogId){
        try {
            blogMapper.addLike(blogId);
            return true;
        }
        catch (Exception e){
            return false;
        }
    }

    public boolean deleteLike(Long blogId){
        try {
            blogMapper.deleteLike(blogId);
            return true;
        }
        catch (Exception e){
            return false;
        }
    }

    public List<Blog> getHot(){
    return blogMapper.getHot();
    }
}
