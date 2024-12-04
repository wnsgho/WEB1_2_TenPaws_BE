package com.example.tenpaws.domain.user.repositoty;

import com.example.tenpaws.domain.user.entity.OAuth2UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OAuth2UserRepository extends JpaRepository<OAuth2UserEntity, String> {
    Optional<OAuth2UserEntity> findByEmail(String email);
    Optional<OAuth2UserEntity> findByUserId(String userId);
}
