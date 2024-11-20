package com.example.tenpaws.domain.board.repository;

import com.example.tenpaws.domain.board.entity.Announcement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {
}
