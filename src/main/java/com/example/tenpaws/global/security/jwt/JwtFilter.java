package com.example.tenpaws.global.security.jwt;

import com.example.tenpaws.domain.admin.entity.Admin;
import com.example.tenpaws.domain.admin.repository.AdminRepository;
import com.example.tenpaws.domain.shelter.entity.Shelter;
import com.example.tenpaws.domain.shelter.repository.ShelterRepository;
import com.example.tenpaws.domain.user.entity.OAuth2UserEntity;
import com.example.tenpaws.domain.user.entity.User;
import com.example.tenpaws.domain.user.repositoty.OAuth2UserRepository;
import com.example.tenpaws.domain.user.repositoty.UserRepository;
import com.example.tenpaws.global.entity.UserRole;
import com.example.tenpaws.global.security.dto.AdminUserDetails;
import com.example.tenpaws.global.security.dto.NormalUserDetails;
import com.example.tenpaws.global.security.dto.OAuth2UserDetails;
import com.example.tenpaws.global.security.dto.ShelterUserDetails;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final ShelterRepository shelterRepository;
    private final AdminRepository adminRepository;
    private final OAuth2UserRepository oAuth2UserRepository;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        return path.equals("/login") || path.equals("/") || path.equals("/join");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String accessToken = authorizationHeader.substring(7);

        try {
            jwtUtil.isExpired(accessToken);
        } catch (ExpiredJwtException e) {
            sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "만료된 엑세스 토큰 입니다");
            return;
        }

        String category = jwtUtil.getCategory(accessToken);

        if (!category.equals("access") && !category.equals("Social")) {
            sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "유효하지 않은 엑세스 토큰 입니다");
            return;
        }

        String email = jwtUtil.getEmail(accessToken);
        log.info("Extracted email from token : {}", email);
        UserRole role = jwtUtil.getRole(accessToken);
        log.info("Extracted email from role : {}", role);

        // 인증된 사용자의 이메일을 로그로 출력하여 디버깅
        System.out.println("Authenticated email: " + email);

        if (role == UserRole.ROLE_USER) {
            if (category.equals("Social")) {
                OAuth2UserEntity oAuth2UserEntity = oAuth2UserRepository.findByEmail(email)
                        .orElseThrow(() -> new RuntimeException("OAuth2 user not found"));
                log.info("--------------- {} ------------", oAuth2UserEntity);

                oAuth2UserEntity.changeEmail(email);
                oAuth2UserEntity.changeUserRole(role);
                OAuth2UserDetails oAuth2UserDetails = new OAuth2UserDetails(oAuth2UserEntity);

                Authentication authToken = new UsernamePasswordAuthenticationToken(oAuth2UserDetails, null, oAuth2UserDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authToken);
            } else {
                User user = userRepository.findByEmail(email)
                        .orElseThrow(() -> new RuntimeException("Normal user not found"));

                user.changeEmail(email);
                user.changeUserRole(role);
                NormalUserDetails normalUserDetails = new NormalUserDetails(user);

                Authentication authToken = new UsernamePasswordAuthenticationToken(normalUserDetails, null, normalUserDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        } else if (role == UserRole.ROLE_SHELTER) {
            Shelter shelter = shelterRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Shelter not found"));

            shelter.changeEmail(email);
            shelter.changeUserRole(role);
            ShelterUserDetails shelterUserDetails = new ShelterUserDetails(shelter);

            Authentication authToken = new UsernamePasswordAuthenticationToken(shelterUserDetails, null, shelterUserDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authToken);
        } else if (role == UserRole.ROLE_ADMIN || role == UserRole.ROLE_SUPER_ADMIN) {
            Admin admin;

            // 슈퍼 어드민이 아니라면 일반 어드민 처리
            if (role == UserRole.ROLE_SUPER_ADMIN) {
                admin = adminRepository.findByEmail(email)
                        .orElseThrow(() -> new RuntimeException("Super Admin not found"));

                // 슈퍼 어드민에 대한 추가적인 처리 로직 필요시 추가
                log.info("Super Admin logged in: {}", email);
            } else {
                admin = adminRepository.findByEmail(email)
                        .orElseThrow(() -> new RuntimeException("Admin not found"));
            }

            admin.changeEmail(email);
            admin.changeUserRole(role);
            AdminUserDetails adminUserDetails = new AdminUserDetails(admin);

            Authentication authToken = new UsernamePasswordAuthenticationToken(adminUserDetails, null, adminUserDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }

        filterChain.doFilter(request, response);
    }

    private void sendErrorResponse(HttpServletResponse response, int statusCode, String message) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(statusCode);
        response.getWriter().write(String.format("{\"error\": \"%s\"}", message));
    }
}
