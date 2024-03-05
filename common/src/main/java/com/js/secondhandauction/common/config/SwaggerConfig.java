package com.js.secondhandauction.common.config;

import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {


//        SecurityScheme basicAuth = new SecurityScheme()
//                .type(SecurityScheme.Type.APIKEY)
//                .in(SecurityScheme.In.COOKIE)
//                .name("JSSESSIONID");
//        SecurityRequirement securityRequirement = new SecurityRequirement().addList("basicAuth");

        return new OpenAPI()
                .info(new io.swagger.v3.oas.models.info.Info()
                        .title("중고거래장터 API")
                        .description("중고거래장터 API 명세서입니다.")
                        .version("v1.0"));
//        .components(new io.swagger.v3.oas.models.Components()
//        .addSecuritySchemes("basicAuth", basicAuth))
//        .addSecurityItem(securityRequirement);
    }
}
