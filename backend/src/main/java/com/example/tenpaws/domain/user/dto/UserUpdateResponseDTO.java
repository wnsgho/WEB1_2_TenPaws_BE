package com.example.tenpaws.domain.user.dto;

import com.example.tenpaws.domain.pet.species.Species;
import com.example.tenpaws.domain.user.entity.User;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class UserUpdateResponseDTO {
    private String username;
    private String password;
    private LocalDate birthDate;
    private String phoneNumber;
    private String address;
    private Species species;
    private String preferredSize;
    private String preferredPersonality;
    private Integer preferredExerciseLevel;

    public static UserUpdateResponseDTO fromEntity(User user) {
        return UserUpdateResponseDTO.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .birthDate(user.getBirthDate())
                .phoneNumber(user.getPhoneNumber())
                .address(user.getAddress())
                .species(user.getSpecies())
                .preferredSize(user.getPreferredSize())
                .preferredPersonality(user.getPreferredPersonality())
                .preferredExerciseLevel(user.getPreferredExerciseLevel())
                .build();
    }
}
