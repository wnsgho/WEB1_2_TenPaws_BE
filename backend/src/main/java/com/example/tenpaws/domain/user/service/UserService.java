package com.example.tenpaws.domain.user.service;

import com.example.tenpaws.domain.shelter.dto.ShelterRequestDTO;
import com.example.tenpaws.domain.user.dto.*;
import com.example.tenpaws.domain.user.entity.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    void registerUser(UserJoinDTO userJoinDTO);
    void registerShelter(ShelterRequestDTO shelterRequestDTO);
    UserResponseDTO getUserById(Long id);
    UserUpdateResponseDTO updateUser(Long id, UserUpdateRequestDTO userUpdateRequestDTO);
    void deleteUser(Long id);
    List<UserResponseDTO> getAllUsers();
    List<OAuth2UserDTO> getAllSocialUsers();
    OAuth2UserDTO getSocialUserInfo(String userId);
    OAuth2UserDTO updateSocialUsername(String userId, UpdateSocialUsernameRequestDTO requestDTO);
}
