package com.lam.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @功能名称: 全局请求结果
 * @文件名称: ResultCode.java
 * @Date: 2020/03/14 1:25 下午
 * @Author: jingmao.li
 */
@Getter
@AllArgsConstructor
public enum ResultCode {

    SUCCESS(0, "成功"),

    PARAM_ILLEGAL(10000, "参数错误"),

    FAIL(1000, "系统错误"),

    UPDATE_FAIL(1001, "更新失败");

    private int code;
    private String msg;
}
