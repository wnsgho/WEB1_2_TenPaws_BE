package com.example.tenpaws.shelter.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ShelterResponseDTO {
    private Long id;          // 보호소 ID
    private String username;         // 이름
    private String shelterName;      // 보호소 이름
    private String address;          // 보호소 번호
    private String phoneNumber;      // 보호소 휴대폰 번호
    private String email;            // 보호소 이메일

    // 엔티티 -> DTO 변환 메서드
    public ShelterResponseDTO(Long id, String username, String shelterName, String address, String phoneNumber, String email) {
        this.id = id;
        this.username = username;
        this.shelterName = shelterName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }
}

