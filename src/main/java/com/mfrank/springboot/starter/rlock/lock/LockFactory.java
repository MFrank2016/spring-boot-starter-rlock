package com.mfrank.springboot.starter.rlock.lock;

import com.mfrank.springboot.starter.rlock.annotation.RLock;
import com.mfrank.springboot.starter.rlock.core.LockInfoProvider;
import com.mfrank.springboot.starter.rlock.model.LockInfo;
import org.aspectj.lang.ProceedingJoinPoint;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by kl on 2017/12/29.
 * Content :
 */
public class LockFactory  {
    Logger logger= LoggerFactory.getLogger(getClass());

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private LockInfoProvider lockInfoProvider;

    public Lock getLock(ProceedingJoinPoint joinPoint, RLock rLock){
        LockInfo lockInfo = lockInfoProvider.get(joinPoint, rLock);
        switch (lockInfo.getType()) {
            case Reentrant:
                return new ReentrantLock(redissonClient, lockInfo);
            case Fair:
                return new FairLock(redissonClient, lockInfo);
            case Read:
                return new ReadLock(redissonClient, lockInfo);
            case Write:
                return new WriteLock(redissonClient, lockInfo);
            default:
                return new ReentrantLock(redissonClient, lockInfo);
        }
    }

}
