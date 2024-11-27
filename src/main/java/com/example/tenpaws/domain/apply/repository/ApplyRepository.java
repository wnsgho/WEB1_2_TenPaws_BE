package com.example.tenpaws.domain.apply.repository;

import com.example.tenpaws.domain.apply.entity.Apply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApplyRepository extends JpaRepository<Apply, Long> {
    List<Apply> findAllByPetShelterId(Long shelterId);
}
