package com.lam.Handler;

public class GlobalException extends RuntimeException{

    private int ret;
    private String msg;
    public GlobalException(Throwable cause) {
        super(cause);
        this.ret = -1;
    }


    public GlobalException(String message) {
        super(message);
        this.msg = message;
        this.ret = -1;
    }


    public GlobalException(int ret, String msg) {
        super(msg);
        this.ret = ret;
        this.msg = msg;
    }

    public int getRet() {
        return ret;
    }

    public String getMsg(){
        return msg;
    }

}
