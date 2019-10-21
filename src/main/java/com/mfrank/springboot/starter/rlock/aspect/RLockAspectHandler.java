package com.mfrank.springboot.starter.rlock.aspect;

import com.mfrank.springboot.starter.rlock.annotation.RLock;
import com.mfrank.springboot.starter.rlock.lock.Lock;
import com.mfrank.springboot.starter.rlock.lock.LockFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by kl on 2017/12/29.
 * Content :给添加@KLock切面加锁处理
 */
@Aspect
@Component
public class RLockAspectHandler {

    @Autowired
    LockFactory lockFactory;

    @Around(value = "@annotation(rLock)")
    public Object around(ProceedingJoinPoint joinPoint, RLock rLock) throws Throwable {
        Lock lock = lockFactory.getLock(joinPoint, rLock);
        boolean currentThreadLock = false;
        try {
            currentThreadLock = lock.acquire();
            return joinPoint.proceed();
        } finally {
            if (currentThreadLock) {
                lock.release();
            }
        }
    }
}
