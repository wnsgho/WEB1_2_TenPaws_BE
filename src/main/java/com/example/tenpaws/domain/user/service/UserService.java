package com.example.tenpaws.domain.user.service;

import com.example.tenpaws.domain.shelter.dto.ShelterRequestDTO;
import com.example.tenpaws.domain.user.dto.UserJoinDTO;
import com.example.tenpaws.domain.user.dto.UserResponseDTO;
import com.example.tenpaws.domain.user.dto.UserUpdateRequestDTO;
import com.example.tenpaws.domain.user.dto.UserUpdateResponseDTO;
import com.example.tenpaws.domain.user.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    void registerUser(UserJoinDTO userJoinDTO);
    void registerShelter(ShelterRequestDTO shelterRequestDTO);
    UserResponseDTO getUserById(Long id);
    UserUpdateResponseDTO updateUser(Long id, UserUpdateRequestDTO userUpdateRequestDTO);
    void deleteUser(Long id);
    List<UserResponseDTO> getAllUsers();
}
