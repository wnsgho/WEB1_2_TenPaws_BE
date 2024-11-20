package com.example.tenpaws.faq.service;

import com.example.tenpaws.faq.dto.FaqRequest;
import com.example.tenpaws.faq.dto.FaqResponse;
import com.example.tenpaws.faq.exception.FaqTaskException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class FaqServiceTests {
    @Autowired
    private FaqService faqService;

    @Test
    public void FAQ생성() {
        FaqRequest faqRequest = FaqRequest.builder().content("test").build();

        FaqResponse faqResponse = faqService.create(faqRequest);

        assertNotNull(faqResponse);
    }

    @Test
    public void FAQ생성실패() {
        FaqRequest faqRequest = FaqRequest.builder().content("test").refFaqId(2L).build();

        assertThrows(FaqTaskException.class, () -> {
            faqService.create(faqRequest);
        });
    }

    @Test
    public void FAQ조회() {
        Long faqId = 1L;

        FaqResponse faqResponse = faqService.read(faqId);

        assertNotNull(faqResponse);
    }
}
