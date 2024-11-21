package com.example.tenpaws.domain.board.controller;

import com.example.tenpaws.domain.board.dto.request.CreateAnnouncementRequest;
import com.example.tenpaws.domain.board.dto.request.UpdateAnnouncementRequest;
import com.example.tenpaws.domain.board.dto.response.AnnouncementListViewResponse;
import com.example.tenpaws.domain.board.dto.response.AnnouncementResponse;
import com.example.tenpaws.domain.board.entity.Announcement;
import com.example.tenpaws.domain.board.service.AnnouncementService;
import com.example.tenpaws.global.api.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/announcements")
@RequiredArgsConstructor
public class AnnouncementController {

    private final AnnouncementService announcementService;

    // Create
    @PostMapping
    public ResponseEntity<ApiResponse<Announcement>> createAnnouncement(@RequestBody CreateAnnouncementRequest request) {
        Announcement savedAnnouncement = announcementService.createAnnouncement(request);

        return ResponseEntity.ok()
                .body(ApiResponse.success(savedAnnouncement));
    }

    // Read(Page)
    @GetMapping
    public ResponseEntity<ApiResponse<Page<AnnouncementListViewResponse>>> getAnnouncementList(
            @PageableDefault(size = 20, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {

        return ResponseEntity.ok(ApiResponse.success(announcementService.getList(pageable)));
    }

    // Read(By announcementId)
    @GetMapping("/{announcementId}")
    public ResponseEntity<ApiResponse<AnnouncementResponse>> findAnnouncementById(@PathVariable Long announcementId) {
        Announcement announcement = announcementService.findById(announcementId);

        return ResponseEntity.ok()
                .body(ApiResponse.success(new AnnouncementResponse(announcement)));
    }

    // Update
    @PutMapping("/{announcementId}")
    public ResponseEntity<ApiResponse<Announcement>> updateAnnouncement(
            @PathVariable Long announcementId,
            @RequestBody UpdateAnnouncementRequest request) {
        Announcement updatedAnnouncement = announcementService.update(announcementId, request);

        return ResponseEntity.ok()
                .body(ApiResponse.success(updatedAnnouncement));
    }

    // Delete
    @DeleteMapping("/{announcementId}")
    public ResponseEntity<ApiResponse<Void>> deleteAnnouncement(@PathVariable Long announcementId) {
        announcementService.delete(announcementId);

        return ResponseEntity.ok(ApiResponse.success(null, "Announcement successfully deleted"));
    }
}
