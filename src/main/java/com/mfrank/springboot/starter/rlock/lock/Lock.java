package com.mfrank.springboot.starter.rlock.lock;

/**
 * Created by kl on 2017/12/29.
 */
public interface Lock {

    boolean acquire();

    void release();
}
