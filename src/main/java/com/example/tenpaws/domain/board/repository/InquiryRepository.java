package com.example.tenpaws.domain.board.repository;

import com.example.tenpaws.domain.board.entity.Inquiry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InquiryRepository extends JpaRepository<Inquiry, Long>{
}
