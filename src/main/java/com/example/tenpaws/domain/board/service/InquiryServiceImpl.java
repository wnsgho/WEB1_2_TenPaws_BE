package com.example.tenpaws.domain.board.service;

import com.example.tenpaws.domain.notification.factory.NotificationFactory;
import com.example.tenpaws.domain.notification.service.NotificationService;
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
import com.example.tenpaws.domain.user.repositoty.UserRepository;
import com.example.tenpaws.global.exception.BaseException;
import com.example.tenpaws.global.exception.ErrorCode;
import com.example.tenpaws.global.utils.AuthenticationUtils;
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
    private final NotificationService notificationService;
    private final NotificationFactory notificationFactory;

    @Transactional
    public Inquiry create(CreateInquiryRequest request) {
        Inquiry inquiry;

        if (request.getUserId() != null) {
            // 사용자 정보 조회 및 문의 생성
            User user = userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new BaseException(ErrorCode.MEMBER_NOT_FOUND));
            inquiry = inquiryRepository.save(request.toEntity(user));

            // 관리자에게 문의 등록 알림 전송
            notificationService.create(
                    notificationFactory.createInquirySubmittedNotification(user.getEmail())
            );
        } else if (request.getShelterId() != null) {
            // 보호소 정보 조회 및 문의 생성
            Shelter shelter = shelterRepository.findById(request.getShelterId())
                    .orElseThrow(() -> new BaseException(ErrorCode.SHELTER_NOT_FOUND));
            inquiry = inquiryRepository.save(request.toEntity(shelter));

            // 관리자에게 문의 등록 알림 전송
            notificationService.create(
                    notificationFactory.createInquirySubmittedNotification(
                            shelter.getEmail()
                    )
            );
        } else {
            throw new BaseException(ErrorCode.MEMBER_OR_SHELTER_NOT_FOUND);
        }

        return inquiry;
    }

    public Page<InquiryListViewResponse> getList(Pageable pageable) {
        return inquiryRepository.findAll(pageable)
                .map(InquiryListViewResponse::new);
    }

    @Transactional
    public InquiryDetailResponse findById(Long inquiryId) {
        Inquiry inquiry = inquiryRepository.findById(inquiryId)
                .orElseThrow(() -> new BaseException(ErrorCode.INQUIRY_NOT_FOUND));

        inquiry.incrementViewCount();

        List<CommentResponse> comments = inquiry.getComments().stream()
                .map(CommentResponse::new)
                .collect(Collectors.toList());

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
