package com.example.tenpaws.domain.board.service;

import com.example.tenpaws.domain.board.dto.request.CreateInquiryRequest;
import com.example.tenpaws.domain.board.dto.request.UpdateInquiryRequest;
import com.example.tenpaws.domain.board.dto.response.InquiryDetailResponse;
import com.example.tenpaws.domain.board.dto.response.InquiryListViewResponse;
import com.example.tenpaws.domain.board.entity.Inquiry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface InquiryService {
    Inquiry create(CreateInquiryRequest request);
    Page<InquiryListViewResponse> getList(Pageable pageable);
    InquiryDetailResponse findById(Long inquiryId);
    Inquiry update(Long inquiryId, UpdateInquiryRequest request);
    void delete(Long inquiryId, Long userId, Long shelterId);
}