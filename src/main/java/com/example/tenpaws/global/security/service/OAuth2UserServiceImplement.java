package com.example.tenpaws.global.security.service;

import com.example.tenpaws.domain.user.entity.OAuth2UserEntity;
import com.example.tenpaws.domain.user.repositoty.OAuth2UserRepository;
import com.example.tenpaws.global.security.entity.CustomOAuth2User;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class OAuth2UserServiceImplement extends DefaultOAuth2UserService {

    private final OAuth2UserRepository oAuth2UserRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest request) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(request);
        String oauthClientName = request.getClientRegistration().getClientName();

        try {
            System.out.println(new ObjectMapper().writeValueAsString(oAuth2User.getAttributes()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        OAuth2UserEntity oAuth2UserEntity = null;
        String userId = null;
        String email = "empty@email.com";

        if (oauthClientName.equals("kakao")) {
            Map<String, Object> kakaoAccount = (Map<String, Object>) oAuth2User.getAttributes().get("kakao_account");
            if (kakaoAccount != null && (boolean) kakaoAccount.get("is_email_valid")) {
                email = (String) kakaoAccount.get("email");
            }
            userId = "kakao_" + oAuth2User.getAttributes().get("id");
            oAuth2UserEntity = new OAuth2UserEntity(userId, email, "kakao");
        }

        if (oauthClientName.equals("naver")) {
            Map<String,String> responseMap = (Map<String, String>) oAuth2User.getAttributes().get("response");
            userId = "naver_" + responseMap.get("id").substring(0,14);
            email = responseMap.get("email");
            oAuth2UserEntity = new OAuth2UserEntity(userId, email, "naver");
        }

        oAuth2UserRepository.save(oAuth2UserEntity);

        return new CustomOAuth2User(userId);
    }
}
