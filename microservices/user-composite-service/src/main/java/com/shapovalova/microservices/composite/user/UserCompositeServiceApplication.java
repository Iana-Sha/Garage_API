package com.shapovalova.microservices.composite.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@ComponentScan("com.shapovalova")
public class UserCompositeServiceApplication {

    @Bean
    RestTemplate restTemplate() {return new RestTemplate();}
    public static void main(String[] args) {
        SpringApplication.run(UserCompositeServiceApplication.class, args);
    }

}
