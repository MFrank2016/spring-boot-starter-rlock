package com.mfrank.springboot.starter.rlock;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class SpringBootStarterRlockApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootStarterRlockApplication.class, args);
        log.info("容器启动成功");
    }

}
