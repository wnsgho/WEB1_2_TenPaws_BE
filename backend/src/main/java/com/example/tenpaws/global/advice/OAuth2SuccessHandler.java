package com.example.tenpaws.global.advice;

import com.example.tenpaws.global.security.entity.CustomOAuth2User;
import com.example.tenpaws.global.security.jwt.JwtUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {
        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        String userId = oAuth2User.getName();
        String email = oAuth2User.getEmail();
        String token = jwtUtil.createSocialJwt(userId, email);

        // 요청에서 기존 토큰을 가져와서 블랙리스트에서 삭제
        String accessTokenFromRequest = getAccessTokenFromHeader(request);
        if (accessTokenFromRequest != null && jwtUtil.isBlacklisted(accessTokenFromRequest)) {
            jwtUtil.removeFromBlacklist(accessTokenFromRequest);
        }

        // 소셜 유저에 대한 새로운 토큰을 리다이렉트 URL에 포함하여 전송
        response.sendRedirect("http://15.164.103.160/auth/oauth-response/" + token + "/3600");
    }

    // 헤더에서 토큰을 추출하는 메소드
    private String getAccessTokenFromHeader(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7); // "Bearer " 부분을 제외한 토큰만 반환
        }
        return null;
    }
}





