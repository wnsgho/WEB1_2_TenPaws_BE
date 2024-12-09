package com.example.tenpaws.global.security.repository;

import com.example.tenpaws.global.security.entity.RefreshEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface RefreshRepository extends JpaRepository<RefreshEntity, Long> {
    RefreshEntity findByEmail(String email);

    @Query("SELECT r FROM RefreshEntity r WHERE r.expiration < :now")
    List<RefreshEntity> findExpiredTokens(@Param("now") String now);

    Boolean existsByRefresh(String refresh);

    @Transactional
    void deleteByRefresh(String refresh);
}
