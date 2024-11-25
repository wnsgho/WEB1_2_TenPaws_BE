package com.example.tenpaws.domain.match.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "matches")
@Getter
@Setter
@NoArgsConstructor
public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long matchId;

    @Column(name = "user_id", nullable = false, length = 50)
    private String userId;

    @Column(name = "pet_id", nullable = false)
    private Long petId;

    // Constructor for easy creation
    @Builder
    public Match(Long matchId, String userId, Long petId) {
        this.matchId = matchId;
        this.userId = userId;
        this.petId = petId;
    }
}