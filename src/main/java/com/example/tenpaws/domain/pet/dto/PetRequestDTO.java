package com.example.tenpaws.domain.pet.dto;

import com.example.tenpaws.domain.pet.entity.Pet;
import com.example.tenpaws.domain.pet.species.Species;
import com.example.tenpaws.domain.shelter.entity.Shelter;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PetRequestDTO {
    private String petName;

    // species 필드를 추가
    private Species species; // Species 필드를 추가

    // @JsonCreator를 이용해 Species를 직접 매핑
    @JsonCreator
    public void setSpecies(@JsonProperty("species") String species) {
        // Species 값을 문자열로 받아서 매핑하는 방식
        this.species = Species.fromString(species);
    }

    private String size;
    private int age;
    private String gender;
    private Boolean neutering;
    private String reason;
    private String preAdoption;
    private Boolean vaccinated;
    private String extra;
    private String personality;
    private int exerciseLevel;
    private List<MultipartFile> images; // Store multiple image files

    public Pet toEntity(Shelter shelter, List<String> imageUrls) {
        return Pet.builder()
                .petName(this.petName)
                .species(this.species) // species 필드 사용
                .size(this.size)
                .age(this.age)
                .gender(this.gender)
                .neutering(this.neutering)
                .reason(this.reason)
                .preAdoption(this.preAdoption)
                .vaccinated(this.vaccinated)
                .extra(this.extra)
                .personality(this.personality)
                .exerciseLevel(this.exerciseLevel)
                .shelter(shelter)
                .imageUrls(imageUrls)
                .build();
    }
}

