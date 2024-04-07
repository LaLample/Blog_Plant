package com.lam.model.entity.dto.resp;


import com.lam.common.ResultCode;
import lombok.Data;

/**
 * @功能名称: TODO
 * @文件名称: Response.java
 * @Date: 2020/03/14 1:02 下午
 * @Author: jingmao.li
 */
@Data
public class Response<T> {

    private int code;

    private boolean isSuccess;

    private String msg;

    private T data;

    public static <T> Response<T> SUCCESS(T data){
        Response<T> response = new Response();
        response.setCode(ResultCode.SUCCESS.getCode());
        response.setSuccess(true);
        response.setMsg(ResultCode.SUCCESS.getMsg());
        response.setData(data);
        return response;
    }

    public static Response<Void> FAIL(ResultCode resultCode){
        if(resultCode == null) return null;

        Response<Void> response = new Response();
        response.setCode(resultCode.getCode());
        response.setSuccess(false);
        response.setMsg(resultCode.getMsg());
        return response;
    }
}
