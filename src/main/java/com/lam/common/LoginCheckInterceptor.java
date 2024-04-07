package com.lam.common;

import com.auth0.jwt.JWT;
import com.lam.Handler.GlobalException;
import com.lam.model.entity.po.User;
import com.lam.service.impl.UserServiceImpl;
import com.lam.utils.JwtUtil;
import com.lam.utils.ThreadLocalUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LoginCheckInterceptor implements HandlerInterceptor {

    @Autowired
    UserServiceImpl userService;

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response ,Object handler) throws Exception{
        if("GET".equals(request.getMethod())){
            return true;
        }
        if(request.getRequestURL() != null && (request.getRequestURL().indexOf("swagger") >= 0 || request.getRequestURL().indexOf("/task") >= 0 || request.getRequestURL().indexOf("/upload") >= 0)){
            return true;
        }
        String token=request.getHeader("token");
        if(!StringUtils.hasLength(token)){
            throw new GlobalException(-6,"登录态异常");
        }
        Claims claims=JwtUtil.parseJWT(token);
        ThreadLocalUtil.set(claims);
        User user=userService.check();
        request.setAttribute("uid",user.getUid());
        request.setAttribute("operator",user.getName());
        return true;
    }



}
