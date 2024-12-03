package com.example.tenpaws.domain.board.service;

import com.example.tenpaws.domain.board.dto.request.CreateAnnouncementRequest;
import com.example.tenpaws.domain.board.dto.request.UpdateAnnouncementRequest;
import com.example.tenpaws.domain.board.dto.response.AnnouncementListViewResponse;
import com.example.tenpaws.domain.board.dto.response.AnnouncementResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AnnouncementService {
    AnnouncementResponse create(CreateAnnouncementRequest request);
    Page<AnnouncementListViewResponse> getList(Pageable pageable);
    AnnouncementResponse findById(Long announcementId);
    AnnouncementResponse update(Long announcementId, UpdateAnnouncementRequest request);
    void delete(Long announcementId);
}
