package com.example.tenpaws.global.security.jwt;

import com.example.tenpaws.global.entity.UserRole;
import com.example.tenpaws.global.security.entity.RefreshEntity;
import com.example.tenpaws.global.security.repository.RefreshRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Date;

@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private static final long ACCESS_TOKEN_EXPIRATION = 86_400_000L; // 24시간, 최종 배포 전 시간 변경해주기
    private static final long REFRESH_TOKEN_EXPIRATION = 86_400_000L; // 24시간

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final RefreshRepository refreshRepository;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        if (email == null || password == null) {
            throw new AuthenticationException("이메일과 비밀번호는 필수입니다") {};
        }

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, password, null);
        return authenticationManager.authenticate(authToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        String email = authentication.getName();

        UserRole userRole = UserRole.valueOf(authentication.getAuthorities().iterator().next().getAuthority());

        String accessTokenFromRequest = getAccessTokenFromHeader(request);

        if (accessTokenFromRequest != null && jwtUtil.isValidAccessToken(accessTokenFromRequest)) {
            return;
        }

        RefreshEntity existingRefreshEntity = refreshRepository.findByEmail(email);
        String refresh;
        if (existingRefreshEntity != null) {
            refresh = existingRefreshEntity.getRefresh();
        } else {
            refresh = jwtUtil.createJwt("refresh", email, userRole, REFRESH_TOKEN_EXPIRATION);
            addRefreshEntity(email, refresh, REFRESH_TOKEN_EXPIRATION);
        }

        String access = jwtUtil.createJwt("access", email, userRole, ACCESS_TOKEN_EXPIRATION);

        response.setHeader("Authorization", "Bearer " + access);
        response.addCookie(createCookie("refresh", refresh));
        response.setStatus(HttpStatus.OK.value());
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());

        String errorMessage = "{\"error\": \"Authentication failed\", \"message\": \"" + failed.getMessage() + "\"}";
        response.getWriter().write(errorMessage);
    }

    private Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24 * 60 * 60); // 하루
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        return cookie;
    }

    private void addRefreshEntity(String email, String refresh, Long expiredMs) {

        Date date = new Date(System.currentTimeMillis() + expiredMs);

        RefreshEntity refreshEntity = new RefreshEntity();
        refreshEntity.setEmail(email);
        refreshEntity.setRefresh(refresh);
        refreshEntity.setExpiration(date.toString());

        refreshRepository.save(refreshEntity);
    }

    private String getAccessTokenFromHeader(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7); // "Bearer " 부분을 제외한 토큰만 반환
        }
        return null;
    }
}
