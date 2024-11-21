package com.example.tenpaws.shelter.dto;

import com.example.tenpaws.shelter.entity.Shelter;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ShelterRequestDTO {
    private String username;         // 이름
    private String pw;         // 비밀번호
    private String shelterName;      // 보호소 이름
    private String address;          // 보호소 번호
    private String phoneNumber;      // 보호소 휴대폰 번호
    private String email;            // 보호소 이메일

    public Shelter toEntity() {
        return Shelter.builder()
                .username(this.username)
                .pw(this.pw)
                .shelterName(this.shelterName)
                .address(this.address)
                .phoneNumber(this.phoneNumber)
                .email(this.email)
                .build();
    }
}

