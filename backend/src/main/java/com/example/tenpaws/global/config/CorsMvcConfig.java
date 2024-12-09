package com.example.tenpaws.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsMvcConfig implements WebMvcConfigurer {

//    @Override
//    public void addCorsMappings(CorsRegistry corsRegistry) {
//        corsRegistry.addMapping("/**")
//                .allowedOrigins("http://localhost:3000")
//                .allowedMethods("*")
//                .allowedHeaders("*")
//                .allowCredentials(true);
//    }
}

//CORS 헤더 확인: 클라이언트에서 API 호출을 했을 때, 응답에 CORS 관련 헤더가 포함되어 있는지 확인합니다. 예를 들어, Access-Control-Allow-Origin 헤더가 올바르게 설정되었는지 점검합니다.
//API 접근 확인: 다른 출처에서 호출한 API가 잘 동작하는지 확인합니다 (예: http://localhost:3000에서 백엔드 API 호출).
// 에러 메시지 확인: 만약 CORS 문제가 발생하면, 브라우저의 개발자 도구에서 CORS 관련 오류 메시지를 확인하여 원인을 파악합니다.

