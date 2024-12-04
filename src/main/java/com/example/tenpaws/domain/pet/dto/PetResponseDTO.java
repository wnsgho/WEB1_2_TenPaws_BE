package com.example.tenpaws.domain.pet.dto;

import com.example.tenpaws.domain.pet.entity.Pet;
import com.example.tenpaws.domain.pet.species.Species;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PetResponseDTO {
    private Long petId;
    private String petName;
    private Species species;
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
    private Long shelterId;
    private List<String> imageUrls;

    public static PetResponseDTO fromEntity(Pet pet) {
        PetResponseDTO dto = new PetResponseDTO();
        dto.setPetId(pet.getId());
        dto.setPetName(pet.getPetName());
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
        dto.setShelterId(pet.getShelter().getId());
        // Convert List<MultipartFile> to List<String> (image URLs)
        if (pet.getImageUrls() != null) {
            dto.setImageUrls(pet.getImageUrls()); // Pet 클래스에는 List<String> imageUrls 필드가 있음
        }
        return dto;
    }

}
