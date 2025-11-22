package com.ohgiraffers.COZYbe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@ConfigurationPropertiesScan
@EnableJpaAuditing
@SpringBootApplication
public class COZYApplication {

    public static void main(String[] args) {
        SpringApplication.run(COZYApplication.class, args);
        System.out.println("Swagger url : http://localhost:8000/swagger-ui/index.html");
    }

}
