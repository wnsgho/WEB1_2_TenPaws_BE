package com.example.tenpaws.domain.user.service;

import com.example.tenpaws.domain.shelter.dto.ShelterRequestDTO;
import com.example.tenpaws.domain.shelter.entity.Shelter;
import com.example.tenpaws.domain.shelter.repository.ShelterRepository;
import com.example.tenpaws.domain.user.dto.*;
import com.example.tenpaws.domain.user.entity.OAuth2UserEntity;
import com.example.tenpaws.domain.user.entity.User;
import com.example.tenpaws.domain.user.repositoty.OAuth2UserRepository;
import com.example.tenpaws.domain.user.repositoty.UserRepository;
import com.example.tenpaws.global.exception.BaseException;
import com.example.tenpaws.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Struct;
import java.util.IllegalFormatCodePointException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ShelterRepository shelterRepository;
    private final OAuth2UserRepository oAuth2UserRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    // 일반 유저 등록
    @Override
    public void registerUser(UserJoinDTO userJoinDTO) {

        if (userRepository.existsByEmail(userJoinDTO.getEmail())) {
            throw new BaseException(ErrorCode.MEMBER_ALREADY_EXIST);
        }

        User user = userJoinDTO.toEntity();
        user.changePassword(bCryptPasswordEncoder.encode(userJoinDTO.getPassword()));

        userRepository.save(user);
    }

    // 보호소 유저 등록
    @Override
    public void registerShelter(ShelterRequestDTO shelterRequestDTO) {

        if (shelterRepository.existsByEmail(shelterRequestDTO.getEmail())) {
            throw new BaseException(ErrorCode.MEMBER_ALREADY_EXIST);
        }

        Shelter shelter = shelterRequestDTO.toEntity();
        shelter.changePassword(bCryptPasswordEncoder.encode(shelterRequestDTO.getPassword()));

        shelterRepository.save(shelter);
    }

    // 일반 유저 단일 조회
    @Override
    @Transactional(readOnly = true)
    public UserResponseDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new BaseException(ErrorCode.MEMBER_NOT_FOUND));
        return UserResponseDTO.fromEntity(user);
    }

    // 일반 유저 정보 수정
    @Override
    public UserUpdateResponseDTO updateUser(Long id, UserUpdateRequestDTO userUpdateRequestDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new BaseException(ErrorCode.MEMBER_NOT_FOUND));

        if (userUpdateRequestDTO.getUsername() != null) {
            user.changeUsername(userUpdateRequestDTO.getUsername());
        }

        if (userUpdateRequestDTO.getPassword() != null) {
            user.changePassword(bCryptPasswordEncoder.encode(userUpdateRequestDTO.getPassword()));
        }

        if (userUpdateRequestDTO.getPhoneNumber() != null) {
            user.changePhoneNumber(userUpdateRequestDTO.getPhoneNumber());
        }

        if (userUpdateRequestDTO.getAddress() != null) {
            user.changeAddress(userUpdateRequestDTO.getAddress());
        }

        if (userUpdateRequestDTO.getBirthDate() != null) {
            user.changeBirthDate(userUpdateRequestDTO.getBirthDate());
        }

        if (userUpdateRequestDTO.getSpecies() != null) {
            user.chageSpecies(userUpdateRequestDTO.getSpecies());
        }

        if (userUpdateRequestDTO.getPreferredSize() != null) {
            user.changePreferredSize(userUpdateRequestDTO.getPreferredSize());
        }

        if (userUpdateRequestDTO.getPreferredPersonality() != null) {
            user.changePreferredPersonality(userUpdateRequestDTO.getPreferredPersonality());
        }

        if (userUpdateRequestDTO.getPreferredExerciseLevel() != null) {
            user.changePreferredExerciseLevel(userUpdateRequestDTO.getPreferredExerciseLevel());
        }

        return UserUpdateResponseDTO.fromEntity(user);
    }

    // 일반 유저 탈퇴
    @Override
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new BaseException(ErrorCode.MEMBER_NOT_FOUND);
        }
        userRepository.deleteById(id);
    }

    // 일반 유저 목록 불러오기 : 관리자 전용 기능
    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDTO> getAllUsers() {
        List<User> users = userRepository.findAll();

        return users.stream()
                .map(UserResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    // 소셜 로그인 유저 목록 불러오기 : 관리자 전용 기능
    @Override
    @Transactional(readOnly = true)
    public List<OAuth2UserDTO> getAllSocialUsers() {
        List<OAuth2UserEntity> oAuth2UserEntities = oAuth2UserRepository.findAll();

        return oAuth2UserEntities.stream()
                .map(OAuth2UserDTO::fromEntity)
                .collect(Collectors.toList());
    }

    // 소셜 로그인 유저 단일 정보 조회
    @Override
    @Transactional(readOnly = true)
    public OAuth2UserDTO getSocialUserInfo(String userId) {
        log.info("가져오는 유저 고유 아이디 정보 : {}", userId);
        OAuth2UserEntity oAuth2UserEntity = oAuth2UserRepository.findByEmail(userId)
                .orElseThrow(() -> new BaseException(ErrorCode.SOCIAL_MEMBER_NOT_FOUND));

        return OAuth2UserDTO.fromEntity(oAuth2UserEntity);
    }

    @Override
    public OAuth2UserDTO updateSocialUsername(String userId, UpdateSocialUsernameRequestDTO requestDTO) {
        OAuth2UserEntity oAuth2UserEntity = oAuth2UserRepository.findByUserId(userId)
                .orElseThrow(() -> new BaseException(ErrorCode.SOCIAL_MEMBER_NOT_FOUND));

        oAuth2UserEntity.changeUsername(requestDTO.getUsername());

        return OAuth2UserDTO.fromEntity(oAuth2UserEntity);
    }

    public boolean isUserOwn(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentEmail = authentication.getName();

        User user = userRepository.findByEmail(currentEmail)
                .orElseThrow(() -> new BaseException(ErrorCode.MEMBER_NOT_FOUND));

        if (!user.getId().equals(id)) {
            throw new BaseException(ErrorCode.MEMBER_NOT_AUTHORIZED);
        }

        return true;
    }
}