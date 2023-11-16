package com.student.userservice.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.*;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@ConditionalOnProperty(value="openApiConfig", havingValue="production")
@Configuration
@RefreshScope
public class OpenApiConfiguration {

    @Value("${springdoc.swagger-ui.oauth.authorizationUrl}")
    private String authorizationUrl;
    @Value("${springdoc.swagger-ui.oauth.tokenUrl}")
    private String tokenUrl;

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .servers(
                        List.of(
                                new Server()
                                        .url("/api/user")
                                        .description("Default server")
                        )
                )
                .info(
                        new Info()
                                .title("User Service APIs")
                                .description("This lists all the User Service API Calls. The Calls are OAuth2 secured")
                                .version("v1.0")
                )
                .components(
                        new Components()
                                .addSecuritySchemes("auth-code-flow", authCodeFlowSchema())
                )
                .addSecurityItem(
                        new SecurityRequirement()
                                .addList("auth-code-flow", List.of("read", "write"))
                );
    }

    private SecurityScheme authCodeFlowSchema() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.OAUTH2)
                .flows(new OAuthFlows()
                        .authorizationCode(
                                new OAuthFlow()
                                        .authorizationUrl(authorizationUrl)
                                        .tokenUrl(tokenUrl)
                                        .scopes(
                                                new Scopes()
                                                        .addString("openid", "openid scope")
                                        )
                        )
                );
    }

}
