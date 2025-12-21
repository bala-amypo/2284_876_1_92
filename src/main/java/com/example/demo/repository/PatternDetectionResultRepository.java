package com.example.demo.repository;

import com.example.demo.model.PatternDetectionResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PatternDetectionResultRepository extends JpaRepository<PatternDetectionResult, Long> {
    @Query("SELECT p FROM PatternDetectionResult p WHERE p.zone.id = ?1 ORDER BY p.analysisDate DESC")
    List<PatternDetectionResult> findByZone_Id(Long zoneId);
}