package com.example.tenpaws.domain.board.service;

import com.example.tenpaws.domain.board.dto.request.CreateAnnouncementRequest;
import com.example.tenpaws.domain.board.dto.request.UpdateAnnouncementRequest;
import com.example.tenpaws.domain.board.dto.response.AnnouncementListViewResponse;
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

    private final AnnouncementRepository announcementRepository;

    @Transactional
    public Announcement createAnnouncement(CreateAnnouncementRequest request) {
        return announcementRepository.save(request.toEntity());
    }

    public Page<AnnouncementListViewResponse> getList(Pageable pageable) {
        return announcementRepository.findAll(pageable)
                .map(AnnouncementListViewResponse::new);
    }

    public Announcement findById(Long announcementId) {
        return announcementRepository.findById(announcementId)
                .orElseThrow(() -> new BaseException(ErrorCode.ANNOUNCEMENT_NOT_FOUND));
    }

    @Transactional
    public Announcement update(Long announcementId, UpdateAnnouncementRequest request) {
        Announcement announcement = announcementRepository.findById(announcementId)
                .orElseThrow(() -> new BaseException(ErrorCode.ANNOUNCEMENT_NOT_FOUND));

        announcement.update(request.getTitle(), request.getContent());
        return announcement;
    }

    @Transactional
    public void delete(Long announcementId) {
        announcementRepository.deleteById(announcementId);
    }
}
