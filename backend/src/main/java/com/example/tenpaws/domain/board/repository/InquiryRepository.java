package com.example.tenpaws.domain.board.repository;

import com.example.tenpaws.domain.board.entity.Inquiry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InquiryRepository extends JpaRepository<Inquiry, Long> {
    Page<Inquiry> findByWriterEmail(String email, Pageable pageable);
}