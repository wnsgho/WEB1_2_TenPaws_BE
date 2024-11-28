package com.example.tenpaws.domain.apply.entity;

import com.example.tenpaws.domain.pet.entity.Pet;
import com.example.tenpaws.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "apply")
@Getter
@Setter
@NoArgsConstructor
public class Apply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id", nullable = false)
    private Pet pet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "apply_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date applyDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "apply_status", nullable = false, length = 20)
    private ApplyStatus applyStatus;

    public enum ApplyStatus {
        PENDING,
        UNDER_REVIEW,
        COMPLETED
    }

    // Constructor for easy creation
    @Builder
    public Apply(Long id, Pet pet, User user, java.util.Date applyDate, ApplyStatus applyStatus) {
        this.id = id;
        this.pet = pet;
        this.user = user;
        this.applyDate = applyDate;
        this.applyStatus = applyStatus;
    }
}
