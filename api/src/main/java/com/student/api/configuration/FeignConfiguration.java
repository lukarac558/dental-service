package com.student.api.configuration;

import com.student.api.exception.DentalClinicException;
import feign.Contract;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.codec.ErrorDecoder;
import feign.form.spring.SpringFormEncoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

@Configuration
public class FeignConfiguration {

    @Autowired
    private ObjectFactory<HttpMessageConverters> messageConverters;

    @Bean
    public Encoder feignFormEncoder() {
        return new SpringFormEncoder(new SpringEncoder(messageConverters));
    }

    @Bean
    public Decoder springDecoder() {
        return new ResponseEntityDecoder(new SpringDecoder(messageConverters));
    }

    @Bean
    public Contract feignContract() {
        return new SpringMvcContract();
    }

    @Bean
    public ErrorDecoder errorDecoder(Decoder decoder) {
        return (methodKey, response) -> {
            HttpStatus status = HttpStatus.resolve(response.status());
            DentalClinicException dentalClinicException;
            try {
                Exception exception = (Exception) decoder.decode(response, Exception.class);
                dentalClinicException = new DentalClinicException(exception.getMessage(), exception);
                dentalClinicException.setStatus(status);
                return dentalClinicException;
            } catch (Exception e) {
                DentalClinicException exception = new DentalClinicException("Unknown exception", e);
                exception.setStatus(status);
                return exception;
            }
        };
    }

}
