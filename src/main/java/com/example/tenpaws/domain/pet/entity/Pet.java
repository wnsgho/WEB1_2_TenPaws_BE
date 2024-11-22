package com.example.tenpaws.domain.pet.entity;

import com.example.tenpaws.domain.shelter.entity.Shelter;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "pets")
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pet_id", unique = true, nullable = false)
    private Long id;

    @Column(name = "species", nullable = false, length = 255)
    private String species;

    @Column(name = "size", nullable = false, length = 50)
    private String size;

    @Column(name = "age", nullable = false)
    private int age;

    @Column(name = "personality", nullable = false, length = 255)
    private String personality;

    @Column(name = "exercise_level", nullable = false)
    private int exerciseLevel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shelter_id")
    private Shelter shelter;


    @Builder
    public Pet(Long id, String species, String size, int age) {
        this.id = id;
        this.species = species;
        this.size = size;
        this.age = age;
    }

    // Getters and setters
}