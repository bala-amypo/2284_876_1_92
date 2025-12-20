package com.example.demo.repository;

import com.example.demo.model.CrimeReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CrimeReportRepository extends JpaRepository<CrimeReport, Long> {

    @Query("""
        SELECT c FROM CrimeReport c
        WHERE c.latitude BETWEEN :lat - :range AND :lat + :range
        AND c.longitude BETWEEN :lon - :range AND :lon + :range
    """)
    List<CrimeReport> findCrimesNear(
            @Param("lat") double lat,
            @Param("lon") double lon,
            @Param("range") double range
    );
}
