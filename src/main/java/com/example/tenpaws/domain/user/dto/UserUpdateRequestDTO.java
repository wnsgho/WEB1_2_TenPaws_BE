package com.example.tenpaws.domain.user.dto;

import com.example.tenpaws.domain.pet.species.Species;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class UserUpdateRequestDTO {
    private String username;
    private String password;
    private LocalDate birthDate;
    private String phoneNumber;
    private String address;
    private Species species;
    private String preferredSize;
    private String preferredPersonality;
    private Integer preferredExerciseLevel;
}