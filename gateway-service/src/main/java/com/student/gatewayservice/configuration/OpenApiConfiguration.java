package com.student.gatewayservice.configuration;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class OpenApiConfiguration {
    @Bean
    @Lazy(false)
    public List<GroupedOpenApi> apis(RouteDefinitionLocator locator) {
        return locator.getRouteDefinitions()
                .collectList()
                .blockOptional()
                .orElse(new ArrayList<>())
                .stream()
                .map(RouteDefinition::getId)
                .filter(routeId -> routeId.matches(".*-service"))
                .map(routeId -> routeId.replace("-service", ""))
                .map(routeName -> GroupedOpenApi.builder()
                        .pathsToMatch("/" + routeName + "/**")
                        .group(routeName)
                        .build()
                ).toList();
    }
}
