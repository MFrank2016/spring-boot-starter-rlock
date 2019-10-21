package com.mfrank.springboot.starter.rlock.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = RLockConfig.PREFIX)
public class RLockConfig {

    public static final String PREFIX = "spring.rlock";
    //redisson
    private String address;
    private String password;
    private int database=15;
    private ClusterServer clusterServer;
    private String codec = "org.redisson.codec.JsonJacksonCodec";
    //lock
    private long waitTime = 60;
    private long leaseTime = 60;

    @Data
    public static class ClusterServer{
        private String[] nodeAddresses;
    }
}
