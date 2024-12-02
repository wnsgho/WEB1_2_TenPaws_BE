package com.example.tenpaws.domain.pet.dto;

import com.example.tenpaws.domain.pet.entity.Image;
import com.example.tenpaws.domain.pet.entity.Pet;
import com.example.tenpaws.domain.shelter.entity.Shelter;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PetResponseDTO {
    private Long petId;
    private String species;
    private String size;
    private int age;
    private Boolean gender;
    private Boolean neutering;
    private String reason;
    private String preAdoption;
    private Boolean vaccinated;
    private String extra;
    private String personality;
    private int exerciseLevel;
    private List<String> imageUrls;


    public static PetResponseDTO fromEntity(Pet pet) {
        PetResponseDTO dto = new PetResponseDTO();
        dto.setPetId(pet.getId());
        dto.setSpecies(pet.getSpecies());
        dto.setSize(pet.getSize());
        dto.setAge(pet.getAge());
        dto.setGender(pet.getGender());
        dto.setNeutering(pet.getNeutering());
        dto.setReason(pet.getReason());
        dto.setPreAdoption(pet.getPreAdoption());
        dto.setVaccinated(pet.getVaccinated());
        dto.setExtra(pet.getExtra());
        dto.setPersonality(pet.getPersonality());
        dto.setExerciseLevel(pet.getExerciseLevel());
        if (pet.getImages() != null) {
            dto.setImageUrls(pet.getImages()
                    .stream()
                    .map(Image::getImageUrl)
                    .collect(Collectors.toList()));
        }
        return dto;
    }
}
