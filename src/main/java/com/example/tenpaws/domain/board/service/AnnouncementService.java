package com.example.tenpaws.domain.board.service;

import com.example.tenpaws.domain.board.dto.request.CreateAnnouncementRequest;
import com.example.tenpaws.domain.board.dto.request.UpdateAnnouncementRequest;
import com.example.tenpaws.domain.board.dto.response.AnnouncementListViewResponse;
import com.example.tenpaws.domain.board.entity.Announcement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AnnouncementService {
    Announcement createAnnouncement(CreateAnnouncementRequest request);
    Page<AnnouncementListViewResponse> getList(Pageable pageable);
    Announcement findById(Long announcementId);
    Announcement update(Long announcementId, UpdateAnnouncementRequest request);
    void delete(Long announcementId);
}
