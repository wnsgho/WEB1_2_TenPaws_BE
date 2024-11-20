package com.example.tenpaws.domain.user.service;

import com.example.tenpaws.domain.user.dto.UserJoinDTO;

public interface UserService {
    void registerUser(UserJoinDTO userJoinDTO);
}
