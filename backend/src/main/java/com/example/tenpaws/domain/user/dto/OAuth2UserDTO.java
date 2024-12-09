package com.example.tenpaws.domain.user.dto;

import com.example.tenpaws.domain.user.entity.OAuth2UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OAuth2UserDTO {

    private String userId;
    private String email;
    private String type;
    private String role;
    private String username;

    public static OAuth2UserDTO fromEntity(OAuth2UserEntity oAuth2UserEntity) {
        return new OAuth2UserDTO(
                oAuth2UserEntity.getUserId(),
                oAuth2UserEntity.getEmail(),
                oAuth2UserEntity.getType(),
                oAuth2UserEntity.getRole(),
                oAuth2UserEntity.getUsername()
        );
    }
}
