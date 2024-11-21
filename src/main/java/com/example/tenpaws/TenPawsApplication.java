package com.example.tenpaws;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TenPawsApplication {

    public static void main(String[] args) {
        SpringApplication.run(TenPawsApplication.class, args);
    }

}
