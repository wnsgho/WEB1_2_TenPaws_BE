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
public class FaqResponse {
    @NotBlank
    private Long faqId;

    @NotBlank
    private String content;

    private Long refFaqId;

    public FaqResponse(Faq faq) {
        this.faqId = faq.getFaqId();
        this.content = faq.getContent();
        this.refFaqId = faq.getParent() == null ? null : faq.getParent().getFaqId();
    }
}
