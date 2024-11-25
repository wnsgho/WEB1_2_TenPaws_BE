package com.example.tenpaws.global.interceptor;

import com.example.tenpaws.global.security.jwt.JwtUtil;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Log4j2
public class JwtChannelInterceptor implements ChannelInterceptor {
    private final JwtUtil jwtUtil;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            List<String> authorization = accessor.getNativeHeader("Authorization");
            if (authorization != null && !authorization.isEmpty()) {
                String token = authorization.get(0).replace("Bearer ", "");
                try {
                    String email = jwtUtil.getEmail(token);
                    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(email, null, null);
                    SecurityContextHolder.getContext().setAuthentication(auth);
                } catch (JwtException e) {
                    log.error("Invalid JWT: {}", e.getMessage());
                    return null;
                } catch (Exception e) {
                    log.error("Unexpected Error: {}", e.getMessage());
                    return null;
                }
            } else {
                log.error("Authorization header is missing");
                return null;
            }
        }
        return message;
    }
}
