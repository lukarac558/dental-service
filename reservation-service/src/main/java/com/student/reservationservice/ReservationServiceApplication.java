package com.student.reservationservice;

import com.student.api.configuration.FeignConfiguration;
import com.student.api.exception.handler.ExceptionHandler;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.ControllerAdvice;

@SpringBootApplication
@EnableFeignClients
@Import({ExceptionHandler.class, FeignConfiguration.class})
public class ReservationServiceApplication {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    public static void main(String[] args) {
        SpringApplication.run(ReservationServiceApplication.class, args);
    }
}
