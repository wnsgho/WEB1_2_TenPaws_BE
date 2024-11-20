package com.example.tenpaws.faq.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "faqs")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Faq {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long faqId;

    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Faq.class)
    private Long refFaqId;

    @OneToMany(mappedBy = "refFaqId", cascade = CascadeType.ALL)
    private List<Faq> subFaqs;

    public void changeContent(String content) {
        this.content = content;
    }
}
