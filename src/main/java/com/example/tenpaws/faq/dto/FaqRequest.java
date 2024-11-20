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

    private Long refFaqId;

    public FaqRequest(Faq faq) {
        this.faqId = faq.getFaqId();
        this.content = faq.getContent();
        this.refFaqId = faq.getRefFaqId();
    }

    public Faq toEntity() {
        return Faq.builder()
                .faqId(faqId)
                .content(content)
                .refFaqId(refFaqId)
                .build();
    }
}
