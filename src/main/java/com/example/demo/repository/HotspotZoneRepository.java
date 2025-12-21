package com.example.demo.repository;

import com.example.demo.model.HotspotZone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface HotspotZoneRepository extends JpaRepository<HotspotZone, Long> {
    boolean existsByZoneName(String zoneName);
    Optional<HotspotZone> findByZoneName(String zoneName);
    List<HotspotZone> findBySeverityLevel(String level);
}