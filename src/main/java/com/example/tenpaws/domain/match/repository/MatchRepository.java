package com.example.tenpaws.domain.match.repository;

import com.example.tenpaws.domain.match.entity.Match;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchRepository extends JpaRepository<Match, Long> {
}
