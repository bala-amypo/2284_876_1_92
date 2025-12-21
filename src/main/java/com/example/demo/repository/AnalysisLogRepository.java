package com.example.demo.repository;

import com.example.demo.model.AnalysisLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AnalysisLogRepository extends JpaRepository<AnalysisLog, Long> {
    @Query("SELECT a FROM AnalysisLog a WHERE a.zone.id = ?1 ORDER BY a.loggedAt DESC")
    List<AnalysisLog> findByZone_Id(Long zoneId);
}