package com.example.tenpaws.global.advice;

import com.example.tenpaws.global.security.entity.CustomOAuth2User;
import com.example.tenpaws.global.security.provider.JwtProvider;
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

    private final JwtProvider jwtProvider;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {
        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        String userId = oAuth2User.getName();
        String token = jwtProvider.create(userId);

        // 로그인 성공 후, 발급된 토큰을 포함한 url 주소로 리다이렉팅
        // 프론트 팀에서는 적절하게 해당 토큰을 저장하고, 유저가 서비스를 사용 가능하게 페이지 구축 또는 다른 주소로 연결
        response.sendRedirect("http://localhost:3000/auth/oauth-response/" + token + "/3600");
    }
}

//리디렉션 URL은 백엔드에서 제공하는 http://localhost:3000/auth/oauth-response/{token}/3600와 같은 URL로, token 부분은 백엔드에서 생성된 JWT 토큰이고 /3600은 만료 시간을 나타냅니다.
//프론트엔드는 이 URL에서 토큰을 받아서 localStorage 또는 상태 관리(예: Redux, Context API)에 저장하여 이후의 요청에 사용합니다.
//만료 시간(3600초)을 함께 보내는 경우, 토큰의 유효 기간을 추적하거나, 만료되었을 때 토큰 갱신을 처리할 수 있습니다.
//프론트엔드는 이 URL을 통해 백엔드에서 받은 JWT 토큰을 활용하여 로그인 상태를 유지하고, 이후 보호된 리소스에 대한 요청을 인증된 사용자로 처리할 수 있습니다.






