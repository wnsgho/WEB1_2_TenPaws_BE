package com.example.tenpaws.domain.board.repository;

import com.example.tenpaws.domain.board.entity.Inquiry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface InquiryRepository extends JpaRepository<Inquiry, Long> {
    Page<Inquiry> findByUserEmail(String email, Pageable pageable);
    Page<Inquiry> findByShelterEmail(String email, Pageable pageable);

    @Query("SELECT i FROM Inquiry i WHERE " +
            "(i.user IS NOT NULL AND i.user.email = :email) OR " +
            "(i.shelter IS NOT NULL AND i.shelter.email = :email)")
    Page<Inquiry> findByWriterEmail(@Param("email") String email, Pageable pageable);
}