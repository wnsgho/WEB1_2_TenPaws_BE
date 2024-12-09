package com.example.tenpaws.domain.apply.dto;

import com.example.tenpaws.domain.apply.entity.Apply;
import com.example.tenpaws.domain.pet.dto.PetResponseDTO;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class ApplyDto {
    private Long id;
    private PetResponseDTO pet;
    private Long userId;
    private Date applyDate;
    private String applyStatus;

    public static ApplyDto fromEntity(Apply apply) {
        return ApplyDto.builder()
                .id(apply.getId())
                .pet(PetResponseDTO.fromEntity(apply.getPet()))
                .userId(apply.getUser().getId())
                .applyDate(apply.getApplyDate())
                .applyStatus(apply.getApplyStatus().name())
                .build();
    }
}
