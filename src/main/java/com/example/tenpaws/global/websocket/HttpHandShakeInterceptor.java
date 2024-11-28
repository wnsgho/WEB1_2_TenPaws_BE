package com.example.tenpaws.global.websocket;

import com.example.tenpaws.domain.user.entity.User;
import com.example.tenpaws.global.security.dto.NormalUserDetails;
import com.example.tenpaws.global.security.jwt.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Component
@RequiredArgsConstructor
@Log4j2
public class HttpHandShakeInterceptor implements HandshakeInterceptor {
    private final JwtUtil jwtUtil;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        if (request instanceof ServletServerHttpRequest servletRequest) {
            HttpServletRequest httpRequest = servletRequest.getServletRequest();
            String token = httpRequest.getParameter("token");
            if (token != null && !jwtUtil.isExpired(token)) {
                User user = User.builder()
                        .email(jwtUtil.getEmail(token))
                        .userRole(jwtUtil.getRole(token))
                        .build();
                NormalUserDetails normalUserDetails = new NormalUserDetails(user);
                Authentication authToken = new UsernamePasswordAuthenticationToken(normalUserDetails, null, normalUserDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authToken);
                attributes.put("user", jwtUtil.getEmail(token));
                return true;
            } else {
                log.error("JWT token would be null or expired");
            }
        }
        return false;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

    }
}
