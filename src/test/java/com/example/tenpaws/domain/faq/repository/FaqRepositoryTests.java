package com.example.tenpaws.domain.faq.repository;

import com.example.tenpaws.domain.faq.entity.Faq;
import com.example.tenpaws.domain.faq.repository.FaqRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.IntStream;

@SpringBootTest
public class FaqRepositoryTests {
    @Autowired
    private FaqRepository faqRepository;

    @Test
    @Transactional
    public void testFindByRefFaqId() {
        IntStream.rangeClosed(1,2).forEach(i -> {
            Faq faq = Faq.builder().content("parent" + i).build();
            faqRepository.save(faq);
        });
        IntStream.rangeClosed(1,3).forEach(i -> {
            Faq faq = Faq.builder().content("child" + i).parent(Faq.builder().id(1L).build()).build();
            faqRepository.save(faq);
        });

        Long parentId = 1L;

        faqRepository.findByParentId(parentId).forEach(faq -> {
            System.out.println(faq.toString());
        });
    }
}
