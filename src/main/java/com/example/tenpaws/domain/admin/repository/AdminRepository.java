package com.example.tenpaws.domain.admin.repository;

import com.example.tenpaws.domain.admin.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Long> {
}
