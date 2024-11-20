package com.example.tenpaws.domain.user.service;

import com.example.tenpaws.domain.user.dto.UserJoinDTO;
import com.example.tenpaws.domain.user.entity.User;
import com.example.tenpaws.domain.user.repositoty.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public void registerUser(UserJoinDTO userJoinDTO) {

        if (userRepository.existsByUsername(userJoinDTO.getUsername())) {
            throw new IllegalArgumentException("이미 존재하는 사용자 입니다");
        }

        User user = userJoinDTO.toEntity();
        user.changePassword(bCryptPasswordEncoder.encode(userJoinDTO.getPassword()));

        userRepository.save(user);
    }
}
