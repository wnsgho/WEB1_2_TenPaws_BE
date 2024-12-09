package com.example.tenpaws.global.security.jwt;

import com.example.tenpaws.global.security.repository.RefreshRepository;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@RequiredArgsConstructor
public class CustomLogoutFilter extends GenericFilterBean {

    private final JwtUtil jwtUtil;
    private final RefreshRepository refreshRepository;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        doFilter((HttpServletRequest) servletRequest, (HttpServletResponse) servletResponse, filterChain);
    }

    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {

        // logout 주소인 지 확인
        String requestUri = request.getRequestURI();
        if (!requestUri.equals("/logout")) {
            filterChain.doFilter(request, response);
            return;
        }

        // post 방식으로 들어온 http request인 지 확인
        String requestMethod = request.getMethod();
        if (!requestMethod.equals("POST")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Authorization 헤더에서 Access Token 추출
        String accessToken = getAccessTokenFromHeader(request);
        if (accessToken != null) {
            try {
                jwtUtil.isExpired(accessToken);
                jwtUtil.blacklistToken(accessToken);
                String category = jwtUtil.getCategory(accessToken);
                if (!category.equals("access") && !category.equals("Social")) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    return;
                }
            } catch (ExpiredJwtException e) {
                // 토큰이 만료된 상태라면 처리하지 않고 진행
            }
        }

        // Refresh 토큰 쿠키 추출
        String refreshToken = getRefreshTokenFromCookies(request);
        if (refreshToken != null) {
            try {
                jwtUtil.isExpired(refreshToken);
                String category = jwtUtil.getCategory(refreshToken);
                if (!category.equals("refresh")) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    return;
                }

                // Refresh 토큰 DB 존재 여부 확인
                boolean isExist = refreshRepository.existsByRefresh(refreshToken);
                if (isExist) {
                    refreshRepository.deleteByRefresh(refreshToken);
                }
            } catch (ExpiredJwtException e) {
                // Refresh 토큰이 만료된 경우 무시
            }

            // Refresh 토큰 쿠키 삭제
            Cookie cookie = new Cookie("refresh", null);
            cookie.setMaxAge(0);
            cookie.setPath("/");
            response.addCookie(cookie);
        }

        SecurityContextHolder.clearContext();

        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write("{\"message\": \"Successfully logged out\"}");
    }

    // 헤더에서 Access Token 추출
    private String getAccessTokenFromHeader(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7); // "Bearer " 이후의 토큰 부분 추출
        }
        return null;
    }

    // 쿠키에서 Refresh 토큰 추출
    private String getRefreshTokenFromCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refresh".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
