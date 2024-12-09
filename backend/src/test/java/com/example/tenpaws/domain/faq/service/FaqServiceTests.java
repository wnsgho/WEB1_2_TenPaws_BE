package com.example.tenpaws.domain.faq.service;

import com.example.tenpaws.domain.faq.dto.FaqRequest;
import com.example.tenpaws.domain.faq.dto.FaqResponse;
import com.example.tenpaws.domain.faq.entity.Faq;
import com.example.tenpaws.domain.faq.repository.FaqRepository;
import com.example.tenpaws.global.exception.BaseException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class FaqServiceTests {
    @Autowired
    private FaqService faqService;
    @Autowired
    private FaqRepository faqRepository;

    Long savedId1;
    Long savedId2;

    @BeforeEach
    void setUp() {
        Faq faq = faqRepository.save(Faq.builder()
                .content("parent1")
                .build());
        savedId1 = faq.getId();
        Faq faq1 = faqRepository.save(Faq.builder()
                .content("parent2")
                .build());
        savedId2 = faq1.getId();
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
    @Transactional
    void FAQ생성() {
        FaqRequest faqRequest = FaqRequest.builder().content("test").build();

        FaqResponse faqResponse = faqService.create(faqRequest);

        assertNotNull(faqResponse);
    }

    @Test
    @Transactional
    void FAQ조회() {
        List<FaqResponse> faqResponseList = faqService.read();

        assertFalse(faqResponseList.isEmpty());
    }

    @Test
    @Transactional
    void FAQ수정() {
        FaqRequest faqRequest = FaqRequest.builder().faqId(savedId1).content("test update").build();

        FaqResponse faqResponse = faqService.update(faqRequest);

        assertNotNull(faqResponse);
    }

    @Test
    void FAQ부모아이디로조회() {
        Long parentId = savedId1;

        for (FaqResponse faqResponse : faqService.findByParentId(parentId)) {
            assertNotNull(faqResponse);
        }

    }

    @Test
    @Transactional
    void FAQ삭제() {
        Long faqId = savedId2;

        faqService.delete(faqId);

        assertThrows(BaseException.class, () -> {
            faqService.delete(faqId);
        });
    }

    @Test
    void testFindAll() {
        List<FaqResponse> faqResponseList = faqService.findAll();

        assertEquals(4, faqResponseList.size());
    }
}
