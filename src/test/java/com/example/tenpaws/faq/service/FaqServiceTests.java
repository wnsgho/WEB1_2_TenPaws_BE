package com.example.tenpaws.faq.service;

import com.example.tenpaws.faq.dto.FaqRequest;
import com.example.tenpaws.faq.dto.FaqResponse;
import com.example.tenpaws.faq.entity.Faq;
import com.example.tenpaws.faq.exception.FaqTaskException;
import com.example.tenpaws.faq.repository.FaqRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FaqServiceTests {
    @Autowired
    private FaqService faqService;
    @Autowired
    private FaqRepository faqRepository;

    @BeforeEach
    @Transactional
    void setUp() {
        Faq parentFaq = Faq.builder().content("parent").build();
        Faq saved = faqRepository.save(parentFaq);
        IntStream.rangeClosed(1,3).forEach(i -> {
            Faq childFaq = Faq.builder().content("child" + i).parent(saved).build();
            faqRepository.save(childFaq);
        });
        faqRepository.flush();
    }

    @Test
    @Transactional
    @Order(1)
    void FAQ생성() {
        FaqRequest faqRequest = FaqRequest.builder().content("test").build();

        FaqResponse faqResponse = faqService.create(faqRequest);

        assertNotNull(faqResponse);
    }

    @Test
    @Order(2)
    void FAQ생성실패() {
        FaqRequest faqRequest = FaqRequest.builder().content("test").parentId(2L).build();

        assertThrows(FaqTaskException.class, () -> {
            faqService.create(faqRequest);
        });
    }

    @Test
    @Transactional
    @Order(3)
    void FAQ조회() {
        Long faqId = 6L;

        FaqResponse faqResponse = faqService.read(faqId);

        assertNotNull(faqResponse);
    }

    @Test
    @Order(4)
    void FAQ조회실패() {
        Long faqId = 1000L;

        assertThrows(FaqTaskException.class, () -> {
            faqService.read(faqId);
        });
    }

    @Test
    @Transactional
    @Order(5)
    void FAQ수정() {
        FaqRequest faqRequest = FaqRequest.builder().faqId(6L).content("test update").build();

        FaqResponse faqResponse = faqService.update(faqRequest);

        assertNotNull(faqResponse);
    }

    @Test
    @Order(6)
    void FAQ수정실패() {
        FaqRequest faqRequest = FaqRequest.builder().faqId(1000L).content("test update").build();

        assertThrows(FaqTaskException.class, () -> {
            faqService.update(faqRequest);
        });
    }

    @Test
    @Order(7)
    void FAQ부모아이디로조회() {
        Long parentId = 6L;

        for (FaqResponse faqResponse : faqService.findByParentId(parentId)) {
            assertNotNull(faqResponse);
        }

    }

    @Test
    @Order(8)
    @Transactional
    void FAQ삭제() {
        Long faqId = 6L;

        faqService.delete(faqId);

        assertThrows(FaqTaskException.class, () -> {
            faqService.read(faqId);
        });
    }

    @Test
    @Order(9)
    void FAQ삭제실패() {
        Long faqId = 1000L;

        assertThrows(FaqTaskException.class, () -> {
            faqService.delete(faqId);
        });
    }


}
