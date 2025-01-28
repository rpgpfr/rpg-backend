package com.rpgproject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.mongodb.core.MongoTemplate;

@SpringBootApplication
public class RpgProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(RpgProjectApplication.class, args);
    }

}
