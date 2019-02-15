package com.loesoft.sample.service.b;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class ServiceBApp {

    public static void main(final String[] args) {
        SpringApplication.run(ServiceBApp.class, args);
    }

}
