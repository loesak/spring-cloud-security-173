package com.loesoft.sample.service.a;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
//@EnableOAuth2Client
//@EnableResourceServer
@EnableFeignClients
@EnableConfigurationProperties
public class ServiceAApp {

    public static void main(final String[] args) {
        SpringApplication.run(ServiceAApp.class, args);
    }

}
