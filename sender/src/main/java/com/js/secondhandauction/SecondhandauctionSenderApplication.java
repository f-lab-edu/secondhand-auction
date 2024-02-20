package com.js.secondhandauction;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableCaching // 캐시 사용을 위한 어노테이션
public class SecondhandauctionSenderApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecondhandauctionSenderApplication.class, args);
    }

}
