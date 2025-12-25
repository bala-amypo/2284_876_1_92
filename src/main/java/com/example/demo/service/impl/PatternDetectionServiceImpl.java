package com.example.demo.service.impl;

import com.example.demo.dto.PatternDetectionResultDTO;
import com.example.demo.model.HotspotZone;
import com.example.demo.model.PatternDetectionResult;
import com.example.demo.repository.HotspotZoneRepository;
import com.example.demo.repository.PatternDetectionResultRepository;
import com.example.demo.service.PatternDetectionService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PatternDetectionServiceImpl implements PatternDetectionService {

    private final HotspotZoneRepository hotspotZoneRepository;
    private final PatternDetectionResultRepository patternDetectionResultRepository;

    public PatternDetectionServiceImpl(
            HotspotZoneRepository hotspotZoneRepository,
            PatternDetectionResultRepository patternDetectionResultRepository) {
        this.hotspotZoneRepository = hotspotZoneRepository;
        this.patternDetectionResultRepository = patternDetectionResultRepository;
    }

    @Override
    public PatternDetectionResultDTO detectPattern(Long zoneId) {

        HotspotZone zone = hotspotZoneRepository.findById(zoneId)
                .orElseThrow(() -> new RuntimeException("Hotspot zone not found"));

        // Simple logic (you can enhance later)
        int crimeCount = 5;
        String detectedPattern = crimeCount > 3 ? "HIGH_RISK" : "LOW_RISK";

        PatternDetectionResult result = new PatternDetectionResult(
                zone,
                LocalDate.now(),
                crimeCount,
                detectedPattern
        );

        PatternDetectionResult savedResult =
                patternDetectionResultRepository.save(result);

        return mapToDTO(savedResult);
    }

    @Override
    public List<PatternDetectionResultDTO> getResultsByZone(Long zoneId) {
        return patternDetectionResultRepository.findByZone_Id(zoneId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private PatternDetectionResultDTO mapToDTO(PatternDetectionResult result) {
        return new PatternDetectionResultDTO(
                result.getId(),
                result.getZone().getId(), // ✅ FIXED (Long, not String)
                result.getAnalysisDate(),
                result.getCrimeCount(),
                result.getDetectedPattern()
        );
    }
}
