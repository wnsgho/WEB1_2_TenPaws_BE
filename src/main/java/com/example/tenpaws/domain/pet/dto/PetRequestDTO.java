package com.example.tenpaws.domain.pet.dto;

import com.example.tenpaws.domain.pet.entity.Pet;
import lombok.Data;

@Data
public class PetRequestDTO {
    private String species;
    private String size;
    private int age;
    private String personality;
    private int exerciseLevel;

    public Pet toEntity() {
        Pet pet = new Pet();
        pet.setSpecies(this.species);
        pet.setSize(this.size);
        pet.setAge(this.age);
        pet.setPersonality(this.personality);
        pet.setExerciseLevel(this.exerciseLevel);
        return pet;
    }
}

