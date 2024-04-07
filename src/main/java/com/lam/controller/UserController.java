package com.lam.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lam.common.ResultCode;
import com.lam.model.entity.dto.resp.Response;
import com.lam.model.entity.po.User;
import com.lam.model.mapper.UserMapper;
import com.lam.service.impl.UserServiceImpl;
import com.lam.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Api("用户控制器")
public class UserController {

    @Autowired
    UserMapper userMapper;

    @Autowired
    UserServiceImpl userService;

    @PostMapping("/login")
    @ApiOperation("用户登录")
    public Response<String> login(@RequestBody User user){
      String password=DigestUtils.md5Hex(user.getPassword()).toUpperCase();
        QueryWrapper<User> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("isDelete",0);
        queryWrapper.eq("name",user.getName());
        queryWrapper.eq("password",password);
        User u=userMapper.selectOne(queryWrapper);
        if(u==null || u.getState()==0){
          return  Response.SUCCESS(ResultCode.FAIL.getMsg());
        }
        else{
            Map<String,Object> claim=new HashMap<>();
            claim.put("uid",u.getUid());
            claim.put("name",u.getName());
            claim.put("state",u.getState());
            String jwt= JwtUtil.generateJwt(claim);
            return Response.SUCCESS(jwt);
        }
    }

    @PostMapping("/add")
    @ApiOperation("用户注册")
    public Response<Void> add(@RequestBody @Validated User user){
        userService.add(user);
        return Response.SUCCESS(null);
    }

//    @PostMapping("/test")
//    public Response<Void> Te(@RequestParam String tes){
//        Claims claims=JwtUtil.parseJWT(tes);
//        System.out.println(claims.toString());
//        System.out.println(claims.get("uid").toString());
//        return  Response.SUCCESS(null);
//    }

}
