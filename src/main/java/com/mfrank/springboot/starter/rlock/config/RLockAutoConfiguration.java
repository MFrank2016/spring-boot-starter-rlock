package com.mfrank.springboot.starter.rlock.config;

import com.mfrank.springboot.starter.rlock.aspect.RLockAspectHandler;
import com.mfrank.springboot.starter.rlock.core.BusinessKeyProvider;
import com.mfrank.springboot.starter.rlock.core.LockInfoProvider;
import com.mfrank.springboot.starter.rlock.lock.LockFactory;
import io.netty.channel.nio.NioEventLoopGroup;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.Codec;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.util.ClassUtils;

@Configuration
@AutoConfigureAfter(RedisAutoConfiguration.class)
@EnableConfigurationProperties(RLockConfig.class)
@Import({RLockAspectHandler.class})
public class RLockAutoConfiguration {

    @Autowired
    private RLockConfig rLockConfig;

    @Bean(destroyMethod = "shutdown") //bean 销毁时不调用shutdown
    @ConditionalOnMissingBean
    RedissonClient redisson() throws Exception {
        Config config = new Config();
        if(rLockConfig.getClusterServer()!=null){
            config.useClusterServers().setPassword(rLockConfig.getPassword())
                    .addNodeAddress(rLockConfig.getClusterServer().getNodeAddresses());
        }else {
            config.useSingleServer().setAddress(rLockConfig.getAddress())
                    .setDatabase(rLockConfig.getDatabase())
                    .setPassword(rLockConfig.getPassword());
        }
        Codec codec=(Codec) ClassUtils.forName(rLockConfig.getCodec(),ClassUtils.getDefaultClassLoader()).newInstance();
        config.setCodec(codec);
        config.setEventLoopGroup(new NioEventLoopGroup());
        return Redisson.create(config);
    }

    @Bean
    public LockInfoProvider lockInfoProvider(){
        return new LockInfoProvider();
    }

    @Bean
    public BusinessKeyProvider businessKeyProvider(){
        return new BusinessKeyProvider();
    }

    @Bean
    public LockFactory lockFactory(){
        return new LockFactory();
    }
}
