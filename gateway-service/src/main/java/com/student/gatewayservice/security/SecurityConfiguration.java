package com.student.gatewayservice.security;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@RefreshScope
@EnableWebFluxSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
   @Bean
   public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity serverHttpSecurity) {
      return serverHttpSecurity.cors(Customizer.withDefaults())
              .csrf(ServerHttpSecurity.CsrfSpec::disable)
              .authorizeExchange(exchange -> exchange
                      .pathMatchers(HttpMethod.GET,"/webjars/**").permitAll()
                      .pathMatchers(HttpMethod.GET,"/swagger-ui.html").permitAll()
                      .pathMatchers(HttpMethod.GET,"/swagger-resources/**").permitAll()
                      .pathMatchers(HttpMethod.GET,"/v3/api-docs/**").permitAll()
                      .pathMatchers(HttpMethod.GET,"/api/reservation/v3/api-docs/**").permitAll()
                      .pathMatchers(HttpMethod.GET,"/api/user/v3/api-docs/**").permitAll()
                      .pathMatchers(HttpMethod.GET,"/api/location/v3/api-docs/**").permitAll()
                      .anyExchange().permitAll()
              )
              .oauth2ResourceServer(oauth2ResourceServer -> oauth2ResourceServer.jwt(jwtSpec -> {}))
              .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
              .build();
   }

    @Bean
    public CorsWebFilter corsWebFilter() {
        final CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.setAllowedOrigins(List.of("http://localhost:8181", "http://localhost:8080", "http://localhost:4200", "http://localhost:4200/*"));
        corsConfig.setAllowedMethods(Arrays.asList("GET", "POST", "OPTIONS", "HEAD", "PUT"));
        corsConfig.addAllowedHeader(CorsConfiguration.ALL);

        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);

        return new CorsWebFilter(source);
    }

}
