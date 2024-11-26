package com.example.tenpaws.domain.faq.service;

import com.example.tenpaws.domain.faq.dto.FaqRequest;
import com.example.tenpaws.domain.faq.dto.FaqResponse;
import com.example.tenpaws.domain.faq.entity.Faq;
import com.example.tenpaws.domain.faq.repository.FaqRepository;
import com.example.tenpaws.global.exception.BaseException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FaqServiceTests {
    @Autowired
    private FaqService faqService;
    @Autowired
    private FaqRepository faqRepository;

    @BeforeAll
    static void setUpBeforeClass(@Autowired FaqRepository faqRepository) throws Exception {
        Faq faq = faqRepository.save(Faq.builder()
                .content("parent1")
                .build());
        faqRepository.save(Faq.builder()
                .content("parent2")
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

        assertEquals(2, faqResponseList.size());
    }

    @Test
    @Transactional
    void FAQ수정() {
        FaqRequest faqRequest = FaqRequest.builder().faqId(1L).content("test update").build();

        FaqResponse faqResponse = faqService.update(faqRequest);

        assertNotNull(faqResponse);
    }

    @Test
    void FAQ부모아이디로조회() {
        Long parentId = 1L;

        for (FaqResponse faqResponse : faqService.findByParentId(parentId)) {
            assertNotNull(faqResponse);
        }

    }

    @Test
    @Transactional
    void FAQ삭제() {
        Long faqId = 1L;

        faqService.delete(faqId);

        assertThrows(BaseException.class, () -> {
            faqService.delete(faqId);
        });
    }
}
