package com.example.demo.service.impl;

import com.example.demo.model.*;
import com.example.demo.repository.*;
import com.example.demo.service.AnalysisLogService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnalysisLogServiceImpl implements AnalysisLogService {

    private final AnalysisLogRepository analysisLogRepository;
    private final HotspotZoneRepository hotspotZoneRepository;

    // ✅ MATCH TEST CONSTRUCTOR
    public AnalysisLogServiceImpl(
            AnalysisLogRepository analysisLogRepository,
            HotspotZoneRepository hotspotZoneRepository) {

        this.analysisLogRepository = analysisLogRepository;
        this.hotspotZoneRepository = hotspotZoneRepository;
    }

    @Override
    public AnalysisLog addLog(Long zoneId, String message) {

        HotspotZone zone = hotspotZoneRepository.findById(zoneId)
                .orElseThrow(() -> new RuntimeException("Zone not found"));

        AnalysisLog log = new AnalysisLog();
        log.setZone(zone);
        log.setMessage(message);

        return analysisLogRepository.save(log);
    }

    @Override
    public List<AnalysisLog> getLogsByZone(Long zoneId) {

        // ✅ USE EXISTING REPOSITORY METHOD
        return analysisLogRepository.findAll()
                .stream()
                .filter(l -> l.getZone() != null &&
                             l.getZone().getId().equals(zoneId))
                .toList();
    }
}
