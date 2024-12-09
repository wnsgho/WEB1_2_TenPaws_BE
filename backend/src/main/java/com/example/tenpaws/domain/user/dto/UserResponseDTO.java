package com.example.tenpaws.domain.user.dto;

import com.example.tenpaws.domain.pet.species.Species;
import com.example.tenpaws.domain.user.entity.User;
import com.example.tenpaws.global.entity.UserRole;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class UserResponseDTO {
    private Long id;
    private String email;
    private String username;
    private LocalDate birthDate;
    private String phoneNumber;
    private String address;
    private Species species;
    private String preferredSize;
    private String preferredPersonality;
    private Integer preferredExerciseLevel;
    private UserRole userRole;

    public static UserResponseDTO fromEntity(User user) {
        return UserResponseDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .birthDate(user.getBirthDate())
                .phoneNumber(user.getPhoneNumber())
                .address(user.getAddress())
                .species(user.getSpecies())
                .preferredSize(user.getPreferredSize())
                .preferredPersonality(user.getPreferredPersonality())
                .preferredExerciseLevel(user.getPreferredExerciseLevel())
                .userRole(user.getUserRole())
                .build();
    }
}
