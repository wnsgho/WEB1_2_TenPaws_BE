package com.example.tenpaws.domain.shelter.dto;

import com.example.tenpaws.domain.shelter.entity.Shelter;
import com.example.tenpaws.global.entity.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ShelterRequestDTO {

    @NotBlank(message = "비밀번호는 필수 항목입니다.")
    private String password;

    @NotBlank(message = "보호소 이름은 필수 항목입니다.")
    private String shelterName;

    @NotBlank(message = "주소는 필수 항목입니다.")
    private String address;

    private String phoneNumber;

    @Email(message = "유효한 이메일을 입력해주세요.")
    private String email;

    @NotNull(message = "사용자 역할은 필수 항목입니다.")
    private UserRole userRole;

    public Shelter toEntity() {
        return Shelter.builder()
                .password(this.password)
                .shelterName(this.shelterName)
                .address(this.address)
                .phoneNumber(this.phoneNumber)
                .email(this.email)
                .userRole(this.userRole)
                .build();
    }
}
