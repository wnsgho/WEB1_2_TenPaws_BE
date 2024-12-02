package com.example.tenpaws.domain.admin.repository;

import com.example.tenpaws.domain.admin.entity.Admin;
import com.example.tenpaws.global.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    int countByUserRole(UserRole userRole);
    Optional<Admin> findByEmail(String email);
}
