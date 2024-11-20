package com.example.tenpaws.faq.dto;

import com.example.tenpaws.faq.entity.Faq;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FaqRequest {
    private Long faqId;

    @NotBlank
    private String content;

    private Long parentId;

    public FaqRequest(Faq faq) {
        this.faqId = faq.getFaqId();
        this.content = faq.getContent();
        this.parentId = faq.getParent().getFaqId();
    }

    public Faq toEntity() {
        return Faq.builder()
                .faqId(faqId)
                .content(content)
                .parent(Faq.builder().faqId(parentId).build())
                .build();
    }
}
