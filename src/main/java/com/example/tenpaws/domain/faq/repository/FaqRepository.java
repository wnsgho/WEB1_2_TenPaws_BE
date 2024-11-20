package com.example.tenpaws.domain.faq.repository;

import com.example.tenpaws.domain.faq.entity.Faq;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FaqRepository extends JpaRepository<Faq, Long> {
    @Query("select f from Faq f where f.parent.id = :parentId")
    List<Faq> findByParentId(Long parentId);
}
