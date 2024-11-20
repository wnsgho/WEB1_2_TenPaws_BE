package com.example.tenpaws.domain.user.service;

import com.example.tenpaws.domain.user.dto.UserJoinDTO;
import com.example.tenpaws.domain.user.entity.User;
import com.example.tenpaws.domain.user.repositoty.UserRepository;
import com.example.tenpaws.global.entity.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private UserJoinDTO userJoinDTO;

    @BeforeEach
    void setUp() {
        userJoinDTO = new UserJoinDTO(
                "username123",
                "password123",
                "user@example.com",
                LocalDate.of(1990, 1, 1),
                "010-1234-5678",
                "123 Street",
                "Friendly",
                5,
                UserRole.ROLE_USER
        );
    }

    @Test
    void registerUser_shouldSaveUser_whenValidData() {
        when(userRepository.existsByUsername(userJoinDTO.getUsername())).thenReturn(false);

        // Given: BCryptPasswordEncoder가 비밀번호를 암호화하도록 설정
        when(bCryptPasswordEncoder.encode(userJoinDTO.getPassword())).thenReturn("encodedPassword");

        User user = userJoinDTO.toEntity();
        user.changePassword("encodedPassword");

        // When: registerUser 메소드 호출
        userService.registerUser(userJoinDTO);

        // Then: UserRepository의 save 메소드가 한 번 호출되어야 한다.
        verify(userRepository, times(1)).save(argThat(savedUser ->
                savedUser.getUsername().equals(user.getUsername()) &&
                        savedUser.getPassword().equals("encodedPassword") &&
                        savedUser.getEmail().equals(user.getEmail()) &&
                        savedUser.getPhoneNumber().equals(user.getPhoneNumber()) &&
                        savedUser.getAddress().equals(user.getAddress()) &&
                        savedUser.getUserRole().equals(user.getUserRole())
        ));
    }

    @Test
    void registerUser_shouldThrowException_whenUsernameExists() {
        // Given: 이미 존재하는 사용자 이름이 있을 경우를 시뮬레이션
        when(userRepository.existsByUsername(userJoinDTO.getUsername())).thenReturn(true);

        // When & Then: IllegalArgumentException이 발생해야 한다.
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.registerUser(userJoinDTO);
        });

        // Then: 예외 메시지가 정확한지 확인
        assertEquals("이미 존재하는 사용자 입니다", exception.getMessage());
    }
}
