package com.example.tenpaws.domain.faq.repository;

import com.example.tenpaws.domain.faq.entity.Faq;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
public class FaqRepositoryTests {
    @Autowired
    private FaqRepository faqRepository;

    Long savedId;

    @BeforeEach
    void setUp() {
        Faq faq = faqRepository.save(Faq.builder()
                .content("parent")
                .build());
        savedId = faq.getId();
        faqRepository.save(Faq.builder()
                .content("child1")
                .parent(faq)
                .build());
        faqRepository.save(Faq.builder()
                .content("child2")
                .parent(faq)
                .build());
    }

    @AfterEach
    void tearDown() {
        faqRepository.deleteAll();
    }

    @Test
    void testFindByParentId() {
        Long parentId = savedId;

        List<Faq> byParentId = faqRepository.findByParentId(parentId);

        Assertions.assertEquals(2, byParentId.size());
    }

    @Test
    @Transactional
    void testFindTopLevel() {
        List<Faq> topLevel = faqRepository.findTopLevel();

        Assertions.assertEquals(1, topLevel.size());
    }
}
