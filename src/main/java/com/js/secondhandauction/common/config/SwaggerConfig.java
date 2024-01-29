package com.js.secondhandauction.common.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {

        SecurityScheme basicAuth = new SecurityScheme()
                .type(SecurityScheme.Type.APIKEY)
                .in(SecurityScheme.In.COOKIE)
                .name("JSSESSIONID");
        SecurityRequirement securityRequirement = new SecurityRequirement().addList("basicAuth");

        return new OpenAPI()
                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes("basicAuth", basicAuth))
                .addSecurityItem(securityRequirement);
    }
}
