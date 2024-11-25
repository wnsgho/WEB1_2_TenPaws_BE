package com.example.tenpaws.domain.apply.entity;

import com.example.tenpaws.domain.pet.entity.Pet;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "pet_applications")
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

    @Column(name = "user_id", nullable = false, length = 50)
    private String userId;

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
    public Apply(Long id, Pet pet, String userId, java.util.Date applyDate, ApplyStatus applyStatus) {
        this.id = id;
        this.pet = pet;
        this.userId = userId;
        this.applyDate = applyDate;
        this.applyStatus = applyStatus;
    }
}
