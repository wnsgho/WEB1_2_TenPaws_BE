package com.example.tenpaws.pet.entity;

import com.example.tenpaws.shelter.entity.Shelter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
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

    @ManyToOne
    @JoinColumn(name = "shelter_id")
    private Shelter shelter;

    // Default constructor required by JPA
    public Pet() {}

    // Constructor with all parameters except 'id'
    public Pet(String species, String size, int age) {
        this.species = species;
        this.size = size;
        this.age = age;
    }

    // Getters and setters
}