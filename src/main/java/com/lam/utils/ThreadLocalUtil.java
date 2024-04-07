package com.lam.utils;

//ThreadLocal工具类
public class ThreadLocalUtil {

    //生成静态threadLocal对象
    private static final ThreadLocal THREAD_LOCAL=new ThreadLocal<>();

    public static void set(Object object){
        THREAD_LOCAL.set(object);
    }
    public static <T> T get(){
       return (T) THREAD_LOCAL.get();
    }

    public static  void remove(){
        THREAD_LOCAL.remove();
    }
}
