package com.example.tenpaws.domain.user.repositoty;

import com.example.tenpaws.domain.user.entity.OAuth2UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OAuth2UserRepository extends JpaRepository<OAuth2UserEntity, String> {
}
