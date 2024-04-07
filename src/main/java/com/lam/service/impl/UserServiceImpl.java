package com.lam.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lam.Handler.GlobalException;
import com.lam.Handler.GlobalExceptionHandler;
import com.lam.model.entity.po.User;
import com.lam.model.mapper.UserMapper;
import com.lam.service.IUserService;
import com.lam.utils.SnowflakeIdWorker;
import com.lam.utils.ThreadLocalUtil;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {


    SnowflakeIdWorker snowflakeIdWorker=new SnowflakeIdWorker();

    @Autowired
    UserMapper userMapper;

    public void add(User u){
        User user=new User();
        BeanUtils.copyProperties(u,user);
        user.setUid(snowflakeIdWorker.nextId());
        user.setState(1);
        user.setPassword(DigestUtils
                .md5Hex(u.getPassword()).toUpperCase());
        int result=userMapper.insert(user);
        if(result <=0){
             new GlobalExceptionHandler();
        }
    }

    public User check(){
        QueryWrapper<User> queryWrapper=new QueryWrapper<>();
        Claims map=ThreadLocalUtil.get();
        Long uid=Long.parseLong(map.get("uid").toString()) ;
        Date ex=map.getExpiration();
        long exp=ex.getTime();
        queryWrapper.eq("uid",uid);
        queryWrapper.eq("isDelete",0);
        ThreadLocalUtil.remove();
        if(userMapper.selectOne(queryWrapper)==null || exp<System.currentTimeMillis()){
            throw  new GlobalException(-1,"登录态异常");
        }
        return userMapper.selectOne(queryWrapper);
    }
}
