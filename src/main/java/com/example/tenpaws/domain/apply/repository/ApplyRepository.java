package com.example.tenpaws.domain.apply.repository;

import com.example.tenpaws.domain.apply.entity.Apply;
import com.example.tenpaws.domain.pet.entity.Pet;
import com.example.tenpaws.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ApplyRepository extends JpaRepository<Apply, Long> {

    // 유저 ID로 모든 신청 조회
    List<Apply> findAllByUserId(Long userId);

    // 보호소 ID로 모든 신청 조회
    List<Apply> findAllByPetShelterId(Long shelterId);

    // 특정 유저가 특정 동물에 이미 신청했는지 확인
    boolean existsByPetAndUser(Pet pet, User user);

    // 특정 동물과 유저, 상태로 신청 검색
    Optional<Apply> findByPetAndUserAndApplyStatus(Pet pet, User user, Apply.ApplyStatus applyStatus);
    // 특정 유저에 대해 거절, 취소된 신청 제외하여 검색
    @Query("SELECT COUNT(a) > 0 FROM Apply a WHERE a.user.id = :userId AND a.pet != :pet AND a.applyStatus NOT IN :applyStatuses")
    boolean existsByUserIdPetNotApplyStatusNotIn(@Param("userId") Long userId,
                                                       @Param("pet") Pet pet,
                                                       @Param("applyStatuses") List<Apply.ApplyStatus> applyStatuses);
    // 특정 동물과 유저에 대해 취소되지 않은 신청이 존재하는지 확인
    boolean existsByPetAndUserAndApplyStatusNotIn(Pet pet, User user, List<Apply.ApplyStatus> applyStatuses);
}
