package com.example.tenpaws.domain.board.entity;

import com.example.tenpaws.domain.shelter.entity.Shelter;
import com.example.tenpaws.domain.user.entity.User;
import com.example.tenpaws.global.exception.BaseException;
import com.example.tenpaws.global.exception.ErrorCode;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "Inquiries")
public class Inquiry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shelter_id")
    private Shelter shelter;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "view_count", nullable = false)
    private Long viewCount;

    @Column(name = "writer_email", nullable = false)
    private String writerEmail;

    @Column(name = "writer_name", nullable = false)
    private String writerName;

    @OneToMany(mappedBy = "inquiry", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @CreatedDate
    private LocalDate created_at;

    @Builder
    public Inquiry(User user, Shelter shelter, String title, String content, Long viewCount) {
        this.user = user;
        this.shelter = shelter;
        this.title = title;
        this.content = content;
        this.viewCount = viewCount;

        if (user != null) {
            this.writerEmail = user.getEmail();
            this.writerName = user.getUsername();
        } else if (shelter != null) {
            this.writerEmail = shelter.getEmail();
            this.writerName = shelter.getShelterName();
        } else {
            throw new BaseException(ErrorCode.WRITER_NOT_FOUND);
        }
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void incrementViewCount() {
        this.viewCount++;
    }

    public void updateWriterInfo() {
        if (user != null) {
            this.writerEmail = user.getEmail();
            this.writerName = user.getUsername();
        } else if (shelter != null) {
            this.writerEmail = shelter.getEmail();
            this.writerName = shelter.getShelterName();
        } else {
            throw new BaseException(ErrorCode.WRITER_NOT_FOUND);
        }
    }
}