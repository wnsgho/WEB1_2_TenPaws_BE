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

    private static final long ACCESS_TOKEN_EXPIRATION = 600_000L; // 10분
    private static final long REFRESH_TOKEN_EXPIRATION = 86_400_000L; // 24시간

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final RefreshRepository refreshRepository;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String username = obtainUsername(request);
        String password = obtainPassword(request);

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password, null);
        return authenticationManager.authenticate(authToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        String username = authentication.getName();

        UserRole userRole = UserRole.valueOf(authentication.getAuthorities().iterator().next().getAuthority());

        RefreshEntity existingRefreshEntity = refreshRepository.findByUsername(username);
        String refresh;
        if (existingRefreshEntity != null) {
            refresh = existingRefreshEntity.getRefresh();
        } else {
            refresh = jwtUtil.createJwt("refresh", username, userRole, REFRESH_TOKEN_EXPIRATION);
            addRefreshEntity(username, refresh, REFRESH_TOKEN_EXPIRATION);
        }

        String access = jwtUtil.createJwt("access", username, userRole, ACCESS_TOKEN_EXPIRATION);

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

    private void addRefreshEntity(String username, String refresh, Long expiredMs) {

        Date date = new Date(System.currentTimeMillis() + expiredMs);

        RefreshEntity refreshEntity = new RefreshEntity();
        refreshEntity.setUsername(username);
        refreshEntity.setRefresh(refresh);
        refreshEntity.setExpiration(date.toString());

        refreshRepository.save(refreshEntity);
    }
}
