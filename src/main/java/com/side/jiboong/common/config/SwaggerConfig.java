package com.side.jiboong.common.config;

import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springdoc.core.utils.SpringDocUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

@Configuration
public class SwaggerConfig {
    private static final String BEARER_KEY = "bearer-key";

    static {
        SpringDocUtils.getConfig()
                .addAnnotationsToIgnore(AuthenticationPrincipal.class)
        ;
    }

    @Bean
    public GroupedOpenApi publicAPI() {
        return GroupedOpenApi.builder()
                .group("public-api")
                .packagesToScan("com.side.jiboong.presentation.api")
                .addOpenApiCustomizer(openApi -> openApi
                        .info(getInfo())
                        .addSecurityItem(new SecurityRequirement().addList(BEARER_KEY))
                        .components(
                                openApi.getComponents()
                                        .addSecuritySchemes(BEARER_KEY,
                                                new SecurityScheme()
                                                        .name(BEARER_KEY)
                                                        .type(SecurityScheme.Type.HTTP)
                                                        .scheme("bearer")
                                                        .bearerFormat("JWT")
                                        )
                        )
                )
                .build();
    }

    private Info getInfo() {
        return new Info()
                .title("ROOF")
                .summary("ROOF API")
                .description("ROOF API에 대한 스웨거 문서입니다.");
    }
}
