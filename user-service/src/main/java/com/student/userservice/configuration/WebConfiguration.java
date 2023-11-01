package com.student.userservice.configuration;

import com.student.api.annotation.extractor.auth.AuthInfoResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new AuthInfoResolver());
        WebMvcConfigurer.super.addArgumentResolvers(resolvers);
    }
}
