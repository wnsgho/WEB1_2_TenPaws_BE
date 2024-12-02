package com.example.tenpaws.domain.board.service;

import com.example.tenpaws.domain.admin.entity.Admin;
import com.example.tenpaws.domain.admin.repository.AdminRepository;
import com.example.tenpaws.domain.board.dto.request.CreateAnnouncementRequest;
import com.example.tenpaws.domain.board.dto.request.UpdateAnnouncementRequest;
import com.example.tenpaws.domain.board.dto.response.AnnouncementListViewResponse;
import com.example.tenpaws.domain.board.dto.response.AnnouncementResponse;
import com.example.tenpaws.domain.board.entity.Announcement;
import com.example.tenpaws.domain.board.repository.AnnouncementRepository;
import com.example.tenpaws.global.exception.BaseException;
import com.example.tenpaws.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AnnouncementServiceImpl implements AnnouncementService {
    private final AdminRepository adminRepository;
    private final AnnouncementRepository announcementRepository;

    @Transactional
    public Announcement create(CreateAnnouncementRequest request) {
        Admin admin = adminRepository.findById(request.getAdminId())
                .orElseThrow(() -> new BaseException(ErrorCode.MEMBER_NOT_FOUND));
        return announcementRepository.save(request.toEntity(admin));
    }

    public Page<AnnouncementListViewResponse> getList(Pageable pageable) {
        return announcementRepository.findAll(pageable)
                .map(AnnouncementListViewResponse::new);
    }

    @Transactional
    public AnnouncementResponse findById(Long announcementId) {
        Announcement announcement = announcementRepository.findById(announcementId)
                .orElseThrow(() -> new BaseException(ErrorCode.ANNOUNCEMENT_NOT_FOUND));

        announcement.incrementViewCount();
        return new AnnouncementResponse(announcement);
    }

    @Transactional
    public Announcement update(Long announcementId, UpdateAnnouncementRequest request) {
        Announcement announcement = announcementRepository.findById(announcementId)
                .orElseThrow(() -> new BaseException(ErrorCode.ANNOUNCEMENT_NOT_FOUND));

        announcement.update(request.getCategory(), request.getTitle(), request.getContent());
        return announcement;
    }

    @Transactional
    public void delete(Long announcementId) {
        announcementRepository.findById(announcementId)
                .orElseThrow(() -> new BaseException(ErrorCode.ANNOUNCEMENT_NOT_FOUND));
        announcementRepository.deleteById(announcementId);
    }
}
