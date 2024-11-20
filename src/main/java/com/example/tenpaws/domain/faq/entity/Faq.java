package com.example.tenpaws.domain.faq.entity;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Faq parent;

    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Faq> children;

    public void changeContent(String content) {
        this.content = content;
    }

    public void changeParent(Faq parent) {
        this.parent = parent;
    }
}
