package com.student.locationservice;

import com.student.api.configuration.FeignConfiguration;
import com.student.api.exception.handler.ExceptionHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.ControllerAdvice;

@SpringBootApplication
@EnableFeignClients
@Import({ExceptionHandler.class, FeignConfiguration.class})
public class LocationServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(LocationServiceApplication.class, args);
    }
}
