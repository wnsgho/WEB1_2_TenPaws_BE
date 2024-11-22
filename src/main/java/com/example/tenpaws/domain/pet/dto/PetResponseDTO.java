package com.example.tenpaws.domain.pet.dto;

import lombok.Data;

@Data
public class PetResponseDTO {
    private Long petId;
    private String species;
    private String size;
    private int age;
    private String personality;
    private int exerciseLevel;
}
