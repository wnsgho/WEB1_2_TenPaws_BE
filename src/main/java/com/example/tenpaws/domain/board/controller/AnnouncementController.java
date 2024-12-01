package com.example.tenpaws.domain.board.controller;

import com.example.tenpaws.domain.board.dto.request.CreateAnnouncementRequest;
import com.example.tenpaws.domain.board.dto.request.UpdateAnnouncementRequest;
import com.example.tenpaws.domain.board.dto.response.AnnouncementListViewResponse;
import com.example.tenpaws.domain.board.dto.response.AnnouncementResponse;
import com.example.tenpaws.domain.board.entity.Announcement;
import com.example.tenpaws.domain.board.service.AnnouncementService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/announcements")
@RequiredArgsConstructor
public class AnnouncementController {
    private final AnnouncementService announcementService;

    // Create
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<AnnouncementResponse> create(@RequestBody CreateAnnouncementRequest request) {
        Announcement savedAnnouncement = announcementService.create(request);
        return ResponseEntity.ok().body(new AnnouncementResponse(savedAnnouncement));
    }

    // Read(Page)
    @GetMapping
    public ResponseEntity<Page<AnnouncementListViewResponse>> getList(
            @PageableDefault(size = 20, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(announcementService.getList(pageable));
    }

    // Read(By announcementId)
    @GetMapping("/{announcementId}")
    public ResponseEntity<AnnouncementResponse> findById(@PathVariable Long announcementId) {
        Announcement announcement = announcementService.findById(announcementId);
        return ResponseEntity.ok().body(new AnnouncementResponse(announcement));
    }

    // Update
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{announcementId}")
    public ResponseEntity<AnnouncementResponse> update(
            @PathVariable Long announcementId,
            @RequestBody UpdateAnnouncementRequest request) {
        Announcement updatedAnnouncement = announcementService.update(announcementId, request);
        return ResponseEntity.ok().body(new AnnouncementResponse(updatedAnnouncement));
    }

    // Delete
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{announcementId}")
    public ResponseEntity<String> delete(@PathVariable Long announcementId) {
        announcementService.delete(announcementId);
        return ResponseEntity.ok("Announcement successfully deleted");
    }
}