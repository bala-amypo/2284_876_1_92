package com.example.demo.repository;

import com.example.demo.model.AnalysisLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnalysisLogRepository extends JpaRepository<AnalysisLog, Long> {

    List<AnalysisLog> findByZone_Id(Long zoneId);
}
