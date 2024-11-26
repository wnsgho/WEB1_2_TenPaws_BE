package com.example.tenpaws.domain.apply.repository;

import com.example.tenpaws.domain.apply.entity.Apply;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplyRepository extends JpaRepository<Apply, Long> {
}
