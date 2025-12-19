package com.example.demo.repository;

import com.example.demo.model.PatternDetectionResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PatternDetectionResultRepository
        extends JpaRepository<PatternDetectionResult, Long> {

    List<PatternDetectionResult> findByZoneId(Long zoneId);
}
