package com.example.hiddenpiece.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// SwaggerConfig.java
@OpenAPIDefinition(
        info = @Info(title = "개발자 로드맵 공유 플랫폼 API 명세서",
                description = "HIDDEN PIECE는 급성장하는 온라인 교육 시장과 개발자의 교육 및 진로 방향에 대한 필요성에 착안하여 탄생\n" +
                        "한 개발자 로드맵 공유 플랫폼입니다. 자신의 학습 방향과 경험을 기록하고 공유함으로써, 다른 사용자들이 이\n" +
                        "를 참고해 자신만의 로드맵을 수립할 수 있습니다. HIDDEN PIECE는 단순히 학습의 순서를 제시하는 것이 아닌,\n" +
                        "실제 경험을 바탕으로 한 세세한 정보와 기술 학습에 대한 인사이트를 제공합니다. 이를 통해 사용자는 학습의\n" +
                        "방향성을 잡을 뿐만 아니라, 개발 분야를 중심으로 새로운 학습 문화를 만들 수 있습니다.",
                version = "v1"))
@RequiredArgsConstructor
@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi chatOpenApi() {
        String[] paths = {"/api/v1/**"};

        return GroupedOpenApi.builder()
                .group("개발자 로드맵 서비스 API v1")
                .pathsToMatch(paths)
                .build();
    }

    private static final String SECURITY_SCHEME_NAME = "authorization";	// 추가

    @Bean
    public OpenAPI swaggerApi() {
        return new OpenAPI()
                .components(new Components()
                        // 여기부터 추가 부분
                        .addSecuritySchemes(SECURITY_SCHEME_NAME, new SecurityScheme()
                                .name(SECURITY_SCHEME_NAME)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")))
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME));
                // 여기까지
//                .info(new Info()
//                        .title("스프링시큐리티 + JWT 예제")
//                        .description("스프링시큐리티와 JWT를 이용한 사용자 인증 예제입니다.")
//                        .version("1.0.0"));
    }
}