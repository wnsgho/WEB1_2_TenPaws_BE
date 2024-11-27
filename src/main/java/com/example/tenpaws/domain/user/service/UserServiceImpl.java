package com.example.tenpaws.domain.user.service;

import com.example.tenpaws.domain.shelter.dto.ShelterRequestDTO;
import com.example.tenpaws.domain.shelter.entity.Shelter;
import com.example.tenpaws.domain.shelter.repository.ShelterRepository;
import com.example.tenpaws.domain.user.dto.UserJoinDTO;
import com.example.tenpaws.domain.user.entity.User;
import com.example.tenpaws.domain.user.repositoty.UserRepository;
import com.example.tenpaws.global.exception.BaseException;
import com.example.tenpaws.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ShelterRepository shelterRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public void registerUser(UserJoinDTO userJoinDTO) {

        if (userRepository.existsByEmail(userJoinDTO.getEmail())) {
            throw new BaseException(ErrorCode.MEMBER_ALREADY_EXIST);
        }

        User user = userJoinDTO.toEntity();
        user.changePassword(bCryptPasswordEncoder.encode(userJoinDTO.getPassword()));

        userRepository.save(user);
    }

    @Override
    public void registerShelter(ShelterRequestDTO shelterRequestDTO) {

        if (shelterRepository.existsByEmail(shelterRequestDTO.getEmail())) {
            throw new BaseException(ErrorCode.MEMBER_ALREADY_EXIST);
        }

        Shelter shelter = shelterRequestDTO.toEntity();
        shelter.changePassword(bCryptPasswordEncoder.encode(shelterRequestDTO.getPw()));

        shelterRepository.save(shelter);
    }
}
