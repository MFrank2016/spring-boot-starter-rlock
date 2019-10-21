package com.mfrank.springboot.starter.rlock.core;

import com.mfrank.springboot.starter.rlock.annotation.RLock;
import com.mfrank.springboot.starter.rlock.config.RLockConfig;
import com.mfrank.springboot.starter.rlock.model.LockInfo;
import com.mfrank.springboot.starter.rlock.model.LockType;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by kl on 2017/12/29.
 */
public class LockInfoProvider {

    public static final String LOCK_NAME_PREFIX = "lock";
    public static final String LOCK_NAME_SEPARATOR = ".";


    @Autowired
    private RLockConfig rLockConfig;

    @Autowired
    private BusinessKeyProvider businessKeyProvider;

    public LockInfo get(ProceedingJoinPoint joinPoint, RLock rLock) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        LockType type= rLock.lockType();
        String businessKeyName=businessKeyProvider.getKeyName(joinPoint, rLock);
        String lockName = LOCK_NAME_PREFIX+LOCK_NAME_SEPARATOR+getName(rLock.name(), signature)+businessKeyName;
        long waitTime = getWaitTime(rLock);
        long leaseTime = getLeaseTime(rLock);
        return new LockInfo(type,lockName,waitTime,leaseTime);
    }

    private String getName(String annotationName, MethodSignature signature) {
        if (annotationName.isEmpty()) {
            return String.format("%s.%s", signature.getDeclaringTypeName(), signature.getMethod().getName());
        } else {
            return annotationName;
        }
    }


    private long getWaitTime(RLock lock) {
        return lock.waitTime() == Long.MIN_VALUE ?
                rLockConfig.getWaitTime() : lock.waitTime();
    }

    private long getLeaseTime(RLock lock) {
        return lock.leaseTime() == Long.MIN_VALUE ?
                rLockConfig.getLeaseTime() : lock.leaseTime();
    }
}
