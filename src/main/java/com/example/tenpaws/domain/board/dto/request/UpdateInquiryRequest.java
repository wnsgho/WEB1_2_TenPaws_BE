package com.example.tenpaws.domain.board.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateInquiryRequest {
    private Long userId;
    private Long shelterId;
    private String title;
    private String content;
}