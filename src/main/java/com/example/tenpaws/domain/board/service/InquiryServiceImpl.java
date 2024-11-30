package com.example.tenpaws.domain.board.service;

import com.example.tenpaws.domain.shelter.entity.Shelter;
import com.example.tenpaws.domain.shelter.repository.ShelterRepository;
import com.example.tenpaws.domain.user.entity.User;
import com.example.tenpaws.domain.board.dto.request.CreateInquiryRequest;
import com.example.tenpaws.domain.board.dto.request.UpdateInquiryRequest;
import com.example.tenpaws.domain.board.dto.response.InquiryDetailResponse;
import com.example.tenpaws.domain.board.dto.response.InquiryListViewResponse;
import com.example.tenpaws.domain.board.dto.response.CommentResponse;
import com.example.tenpaws.domain.board.entity.Inquiry;
import com.example.tenpaws.domain.board.repository.InquiryRepository;
import com.example.tenpaws.domain.board.repository.CommentRepository;
import com.example.tenpaws.domain.user.repositoty.UserRepository;
import com.example.tenpaws.global.exception.BaseException;
import com.example.tenpaws.global.exception.ErrorCode;
import com.example.tenpaws.global.util.AuthenticationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class InquiryServiceImpl implements InquiryService {
    private final UserRepository userRepository;
    private final ShelterRepository shelterRepository;
    private final InquiryRepository inquiryRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public Inquiry create(CreateInquiryRequest request) {
        if (request.getUserId() != null) {
            User user = userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new BaseException(ErrorCode.MEMBER_NOT_FOUND));
            return inquiryRepository.save(request.toEntity(user));
        } else if (request.getShelterId() != null) {
            Shelter shelter = shelterRepository.findById(request.getShelterId())
                    .orElseThrow(() -> new BaseException(ErrorCode.SHELTER_NOT_FOUND));
            return inquiryRepository.save(request.toEntity(shelter));
        } else {
            throw new BaseException(ErrorCode.MEMBER_OR_SHELTER_NOT_FOUND);
        }
    }

    public Page<InquiryListViewResponse> getList(Pageable pageable) {
        return inquiryRepository.findAll(pageable)
                .map(InquiryListViewResponse::new);
    }

    @Transactional
    public InquiryDetailResponse findById(Long inquiryId) {
        Inquiry inquiry = inquiryRepository.findById(inquiryId)
                .orElseThrow(() -> new BaseException(ErrorCode.INQUIRY_NOT_FOUND));

        List<CommentResponse> comments = commentRepository.findByInquiryId(inquiryId)
                .stream()
                .map(CommentResponse::new)
                .collect(Collectors.toList());

        inquiry.incrementViewCount();
        return new InquiryDetailResponse(inquiry, comments);
    }

    @Transactional
    public Inquiry update(Long inquiryId, UpdateInquiryRequest request) {
        Inquiry inquiry = inquiryRepository.findById(inquiryId)
                .orElseThrow(() -> new BaseException(ErrorCode.INQUIRY_NOT_FOUND));
        AuthenticationUtils.validateInquiryWriter(inquiry, request.getUserId(), request.getShelterId());
        inquiry.update(request.getTitle(), request.getContent());
        return inquiry;
    }

    @Transactional
    public void delete(Long inquiryId, Long userId, Long shelterId) {
        Inquiry inquiry = inquiryRepository.findById(inquiryId)
                .orElseThrow(() -> new BaseException(ErrorCode.INQUIRY_NOT_FOUND));
        AuthenticationUtils.validateInquiryWriter(inquiry, userId, shelterId);
        inquiryRepository.deleteById(inquiryId);
    }
}