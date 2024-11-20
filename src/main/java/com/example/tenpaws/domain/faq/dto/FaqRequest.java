package com.example.tenpaws.domain.faq.dto;

import com.example.tenpaws.domain.faq.entity.Faq;
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
        this.faqId = faq.getId();
        this.content = faq.getContent();
        this.parentId = faq.getParent().getId();
    }

    public Faq toEntity() {
        return Faq.builder()
                .id(faqId)
                .content(content)
                .parent(Faq.builder().id(parentId).build())
                .build();
    }
}
