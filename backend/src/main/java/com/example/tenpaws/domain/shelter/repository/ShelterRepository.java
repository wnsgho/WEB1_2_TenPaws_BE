package com.example.tenpaws.domain.shelter.repository;

import com.example.tenpaws.domain.shelter.entity.Shelter;
import com.example.tenpaws.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShelterRepository extends JpaRepository<Shelter, Long> {
    boolean existsByEmail(String email);
    Optional<Shelter> findByEmail(String email);
}
