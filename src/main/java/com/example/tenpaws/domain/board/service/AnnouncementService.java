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

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnnouncementService {

    private final AnnouncementRepository announcementRepository;

    public Announcement save(CreateAnnouncementRequest request) {
        return announcementRepository.save(request.toEntity());
    }

    public Page<AnnouncementListViewResponse> getList(Pageable pageable) {
        return announcementRepository.findAll(pageable)
                .map(AnnouncementListViewResponse::new);
    }

    public Announcement findById(long id) {
        return announcementRepository.findById(id)
                .orElseThrow(() -> new BaseException(ErrorCode.ANNOUNCEMENT_NOT_FOUND));
    }

    @Transactional
    public Announcement update(long id, UpdateAnnouncementRequest request) {
        Announcement announcement = announcementRepository.findById(id)
                .orElseThrow(() -> new BaseException(ErrorCode.ANNOUNCEMENT_NOT_FOUND));

        announcement.update(request.getTitle(), request.getContent());
        return announcement;
    }

    public void delete(long id) {
        announcementRepository.deleteById(id);
    }
}
