package com.example.tenpaws.domain.faq.service;

import com.example.tenpaws.domain.faq.dto.FaqRequest;
import com.example.tenpaws.domain.faq.dto.FaqResponse;
import com.example.tenpaws.domain.faq.entity.Faq;
import com.example.tenpaws.domain.faq.repository.FaqRepository;
import com.example.tenpaws.global.exception.BaseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FaqServiceTests {
    @Autowired
    private FaqService faqService;

    @BeforeAll
    public static void setUp(@Autowired FaqRepository faqRepository) {
        Faq parentFaq = Faq.builder().content("parent").build();
        Faq saved = faqRepository.save(parentFaq);
        IntStream.rangeClosed(1, 3).forEach(i -> {
            Faq childFaq = Faq.builder().content("child" + i).parent(saved).build();
            faqRepository.save(childFaq);
        });
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
        Long faqId = 1L;

        FaqResponse faqResponse = faqService.read(faqId);

        assertNotNull(faqResponse);
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
            faqService.read(faqId);
        });
    }
}
