package com.example.restservice;

import com.example.restservice.properties.ServerFileStorageProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({
        ServerFileStorageProperties.class
})
public class RestserviceApplication {

    private static final Logger logger = LoggerFactory.getLogger(RestserviceApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(RestserviceApplication.class, args);
    }

}
