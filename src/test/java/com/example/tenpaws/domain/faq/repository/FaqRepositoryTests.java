package com.example.tenpaws.domain.faq.repository;

import com.example.tenpaws.domain.faq.entity.Faq;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
public class FaqRepositoryTests {
    @Autowired
    private FaqRepository faqRepository;

    @BeforeAll
    static void setUpBeforeClass(@Autowired FaqRepository faqRepository) throws Exception {
        Faq faq = faqRepository.save(Faq.builder()
                .content("parent")
                .build());
        faqRepository.save(Faq.builder()
                .content("child1")
                .parent(faq)
                .build());
        faqRepository.save(Faq.builder()
                .content("child2")
                .parent(faq)
                .build());
    }

    @AfterAll
    static void tearDownAfterClass(@Autowired FaqRepository faqRepository) throws Exception {
        faqRepository.deleteAll();
    }

    @Test
    void testFindByParentId() {
        Long parentId = 1L;

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
