package com.lam.service;


public interface Ilock {

    public boolean tryLock(long timeOutSec);

    public void unLock();
}
