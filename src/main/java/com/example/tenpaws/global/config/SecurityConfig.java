package com.example.tenpaws.global.config;

import com.example.tenpaws.domain.admin.repository.AdminRepository;
import com.example.tenpaws.domain.shelter.repository.ShelterRepository;
import com.example.tenpaws.domain.user.repositoty.OAuth2UserRepository;
import com.example.tenpaws.domain.user.repositoty.UserRepository;
import com.example.tenpaws.global.advice.OAuth2SuccessHandler;
import com.example.tenpaws.global.security.jwt.JwtFilter;
import com.example.tenpaws.global.security.jwt.JwtUtil;
import com.example.tenpaws.global.security.jwt.LoginFilter;
import com.example.tenpaws.global.security.jwt.CustomLogoutFilter;
import com.example.tenpaws.global.security.repository.RefreshRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final JwtUtil jwtUtil;
    private final RefreshRepository refreshRepository;
    private final DefaultOAuth2UserService oAuth2UserService;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final UserRepository userRepository;
    private final ShelterRepository shelterRepository;
    private final AdminRepository adminRepository;
    private final OAuth2UserRepository oAuth2UserRepository;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        // rest api, stateless 방식 사용하므로 비활성화
        httpSecurity
                .formLogin(AbstractHttpConfigurer::disable);
        httpSecurity
                .httpBasic(AbstractHttpConfigurer::disable);
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable);

        // CORS 설정
        httpSecurity
                .cors((cors) -> cors
                        .configurationSource(new CorsConfigurationSource() {
                            @Override
                            public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                                CorsConfiguration configuration = new CorsConfiguration();
                                configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000", "http://15.164.103.160:8080"));
                                //configuration.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
                                //configuration.setAllowedOrigins(Collections.singletonList("http://15.164.103.160:8080"));
                                configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
                                configuration.setAllowCredentials(true);
                                configuration.setAllowedHeaders(Arrays.asList("*"));
//                                configuration.setAllowedHeaders(Arrays.asList(
//                                        "X-Requested-With",
//                                        "Origin",
//                                        "Content-Type",
//                                        "Accept",
//                                        "Authorization"
//                                ));
                                configuration.setMaxAge(3600L);
                                configuration.setExposedHeaders(Arrays.asList("Authorization"));

                                return configuration;
                            }
                        }));

        // stateless 상태 활성화
        httpSecurity
                .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // 모든 기능 완성되면 그 때 엔드포인트 보고 접근 권한 수정!
        httpSecurity
                .authorizeHttpRequests((auth) -> auth
                        // 웹소켓 관련 설정
                        .requestMatchers("/ws/**").permitAll()
                        // 사진 접근 허용
                        .requestMatchers("/uploads/**").permitAll()
                        .requestMatchers("/images/**").permitAll()
                        // GET 요청에 대한 permitAll 허용
                        .requestMatchers(HttpMethod.GET,
                                "/api/v1/announcements", "/api/v1/announcements/{announcementId}",
                                "/api/v1/inquiries", "/api/v1/inquiries/{inquiryId}",
                                "/api/v1/faqs", "/api/v1/faqs/top-level", "/api/v1/faqs/{parentId}",
                                "/api/v1/features/check-email",
                                "/api/v1/pets", "/api/v1/pets/{petId}",
                                "/" // 홈 화면은 GET 요청만 허용
                        ).permitAll()

                        // POST 요청에 대한 permitAll 허용
                        .requestMatchers(HttpMethod.POST,
                                "/api/v1/users/regular/join", "/api/v1/users/shelter/join",
                                "/login", "/logout", "/api/v1/auth/oauth2/naver", "/api/v1/auth/oauth2/kakao"
                        ).permitAll()

                        // 나머지 요청에 대한 인증 필요
                        .anyRequest().authenticated()
                );


        httpSecurity
                .addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil, refreshRepository), UsernamePasswordAuthenticationFilter.class);

        httpSecurity
                .addFilterBefore(new JwtFilter(jwtUtil, userRepository, shelterRepository, adminRepository, oAuth2UserRepository), LoginFilter.class);

        httpSecurity
                .addFilterBefore(new CustomLogoutFilter(jwtUtil, refreshRepository), LogoutFilter.class);

        // oauth2
        httpSecurity
                .oauth2Login((oauth2) -> oauth2
                        .authorizationEndpoint((endpoint) -> endpoint.baseUri("/api/v1/auth/oauth2"))
                        .redirectionEndpoint((endpoint) -> endpoint
                                .baseUri("/oauth2/callback/*"))
                        .userInfoEndpoint((endpoint) -> endpoint.userService(oAuth2UserService))
                        .successHandler(oAuth2SuccessHandler)
                );

        return httpSecurity.build();
    }
}

