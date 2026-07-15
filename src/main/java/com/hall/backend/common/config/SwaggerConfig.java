package com.hall.backend.common.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    public static final String SECURITY_SCHEME_NAME = "Bearer Authentication";

    @Bean
    public OpenAPI hallOpenApi() {
        return new OpenAPI()
                .info(apiInfo())
                .components(new Components()
                        .addSecuritySchemes(
                                SECURITY_SCHEME_NAME,
                                bearerSecurityScheme()
                        )
                );
    }

    private Info apiInfo() {
        return new Info()
                .title("Hall API")
                .description("""
                        공연 조회, 공연 회차, 좌석, 예약, 결제 및 회원 관리를 위한 REST API입니다.

                        인증이 필요한 API는 Swagger UI 우측 상단의 Authorize 버튼을 누르고
                        Access Token을 입력하여 테스트할 수 있습니다.
                        """)
                .version("v1")
                .contact(new Contact()
                        .name("Hall Backend")
                );
    }

    private SecurityScheme bearerSecurityScheme() {
        return new SecurityScheme()
                .name(SECURITY_SCHEME_NAME)
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .description("JWT Access Token을 입력합니다.");
    }
}
