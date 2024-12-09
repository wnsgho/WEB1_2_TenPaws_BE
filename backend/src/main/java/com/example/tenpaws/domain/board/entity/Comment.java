package com.example.tenpaws.domain.board.entity;

import com.example.tenpaws.domain.admin.entity.Admin;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "Comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inquiry_id", nullable = false)
    private Inquiry inquiry;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id", nullable = false)
    private Admin admin;

    @Column(name = "content", nullable = false)
    private String content;

    @CreatedDate
    private LocalDate created_at;

    @Builder
    public Comment(Inquiry inquiry, Admin admin, String content) {
        this.inquiry = inquiry;
        this.admin = admin;
        this.content = content;
    }

    public void update(String content) {
        this.content = content;
    }
}