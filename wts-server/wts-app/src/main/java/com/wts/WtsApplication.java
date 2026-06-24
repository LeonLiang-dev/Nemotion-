package com.wts;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("com.wts.**.mapper")
@EnableScheduling
public class WtsApplication {

    public static void main(String[] args) {
        SpringApplication.run(WtsApplication.class, args);
    }
}
