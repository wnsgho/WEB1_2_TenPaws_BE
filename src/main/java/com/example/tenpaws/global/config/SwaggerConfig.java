package com.example.tenpaws.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration    // 스프링 실행시 설정파일 읽어드리기 위한 어노테이션
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(apiInfo());
    }

    private Info apiInfo() {
        return new Info()
                .title("TENPAWS_TEAM6_SWAGGER_API")
                .description("프로그래머스 최종 6팀 파이널 프로젝트 : TEAMPAWS")
                .version("1.0.0");
    }
}

// http://localhost:8080/swagger-ui/index.html#/