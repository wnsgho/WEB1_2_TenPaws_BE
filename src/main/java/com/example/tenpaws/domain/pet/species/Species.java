package com.example.tenpaws.domain.pet.species;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public enum Species {
    강아지, 고양이, 무관;

    @JsonCreator
    public static Species fromString(@JsonProperty("species") String species) {
        try {
            return Species.valueOf(species);
        } catch (IllegalArgumentException e) {
            return Species.무관; // or throw an exception or return null based on your design
        }
    }

    @Override
    public String toString() {
        return name();
    }
}

