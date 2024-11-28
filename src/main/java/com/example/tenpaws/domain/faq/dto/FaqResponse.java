package com.example.tenpaws.domain.faq.dto;

import com.example.tenpaws.domain.faq.entity.Faq;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FaqResponse {
    @NotBlank
    private Long faqId;

    @NotBlank
    private String content;

    private Long refFaqId;

    public FaqResponse(Faq faq) {
        this.faqId = faq.getId();
        this.content = faq.getContent();
        this.refFaqId = faq.getParent() == null ? null : faq.getParent().getId();
    }

    @Builder
    public FaqResponse(Long faqId, String content, Long refFaqId) {
        this.faqId = faqId;
        this.content = content;
        this.refFaqId = refFaqId;
    }
}
