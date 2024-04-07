package com.lam.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lam.Handler.GlobalException;
import com.lam.model.entity.po.Blog;
import com.lam.model.entity.po.Redundancy;
import com.lam.model.mapper.RedundancyMapper;
import com.lam.service.IRedundancyService;
import com.lam.utils.SnowflakeIdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RedundancyServiceImpl extends ServiceImpl<RedundancyMapper, Redundancy> implements IRedundancyService {

    @Autowired
    RedundancyMapper redundancyMapper;

    SnowflakeIdWorker snowflakeIdWorker=new SnowflakeIdWorker();

    public void RedundancyMessage(Blog blog){
        Redundancy redundancy=new Redundancy();
        redundancy.setBlogId(blog.getTxtId());
        redundancy.setRedId(snowflakeIdWorker.nextId());
         int count=redundancyMapper.insert(redundancy);
         if(count!=1){
             throw  new GlobalException(-1,"出现异常，数据库操作异常");
         }
    }
}
