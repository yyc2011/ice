package com.ice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class IceApplication {

    public static void main(String[] args) {
        SpringApplication.run(IceApplication.class, args);
    }
}
