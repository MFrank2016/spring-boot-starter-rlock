package com.mfrank.springboot.starter.rlock.lock;

import com.mfrank.springboot.starter.rlock.model.LockInfo;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;

/**
 * Created by kl on 2017/12/29.
 */
public class FairLock implements Lock {

    private static volatile RLock rLock;
    
    private final LockInfo lockInfo;

    private RedissonClient redissonClient;

    public FairLock(RedissonClient redissonClient, LockInfo info) {
        this.redissonClient = redissonClient;
        this.lockInfo = info;
    }

    @Override
    public boolean acquire() {
        try {
            rLock=redissonClient.getFairLock(lockInfo.getName());
            return rLock.tryLock(lockInfo.getWaitTime(), lockInfo.getLeaseTime(), TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            return false;
        }
    }

    @Override
    public void release() {
        if(rLock.isHeldByCurrentThread()){
            rLock.unlockAsync();
        }
    }
}
