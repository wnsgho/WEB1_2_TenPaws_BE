package com.example.tenpaws.pet.dto;

import lombok.Data;

@Data
public class PetRequestDTO {
    private String species;
    private String size;
    private int age;
    private String personality;
    private int exerciseLevel;
}

