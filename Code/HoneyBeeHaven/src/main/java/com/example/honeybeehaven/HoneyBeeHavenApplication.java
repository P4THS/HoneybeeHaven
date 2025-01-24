package com.example.honeybeehaven;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
public class HoneyBeeHavenApplication {

    public static void main(String[] args) {
        SpringApplication.run(HoneyBeeHavenApplication.class, args);
    }

}
