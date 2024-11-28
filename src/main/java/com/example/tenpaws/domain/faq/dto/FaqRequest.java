package com.example.tenpaws.domain.faq.dto;

import com.example.tenpaws.domain.faq.entity.Faq;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FaqRequest {
    private Long faqId;

    @NotBlank
    private String content;

    private Long parentId;

    @Builder
    public FaqRequest(Long faqId, String content, Long parentId) {
        this.faqId = faqId;
        this.content = content;
        this.parentId = parentId;
    }

    public FaqRequest(Faq faq) {
        this.faqId = faq.getId();
        this.content = faq.getContent();
        this.parentId = faq.getParent().getId();
    }

    public Faq toEntity(Faq faq) {
        return Faq.builder()
                .id(faqId)
                .content(content)
                .parent(faq)
                .build();
    }
}
