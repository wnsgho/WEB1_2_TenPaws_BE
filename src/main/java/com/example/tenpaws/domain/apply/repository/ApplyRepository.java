package com.example.tenpaws.domain.apply.repository;

import com.example.tenpaws.domain.apply.entity.Apply;
import com.example.tenpaws.domain.pet.entity.Pet;
import com.example.tenpaws.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApplyRepository extends JpaRepository<Apply, Long> {
    List<Apply> findAllByPetShelterId(Long shelterId);
    boolean existsByPetAndUser(Pet pet, User user);
}
