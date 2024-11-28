package com.example.tenpaws.domain.user.service;

import com.example.tenpaws.domain.shelter.dto.ShelterRequestDTO;
import com.example.tenpaws.domain.shelter.entity.Shelter;
import com.example.tenpaws.domain.shelter.repository.ShelterRepository;
import com.example.tenpaws.domain.user.dto.UserJoinDTO;
import com.example.tenpaws.domain.user.dto.UserResponseDTO;
import com.example.tenpaws.domain.user.dto.UserUpdateRequestDTO;
import com.example.tenpaws.domain.user.dto.UserUpdateResponseDTO;
import com.example.tenpaws.domain.user.entity.User;
import com.example.tenpaws.domain.user.repositoty.UserRepository;
import com.example.tenpaws.global.exception.BaseException;
import com.example.tenpaws.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ShelterRepository shelterRepository;
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
    public List<UserResponseDTO> getAllUsers() {
        List<User> users = userRepository.findAll();

        return users.stream()
                .map(UserResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }
}
