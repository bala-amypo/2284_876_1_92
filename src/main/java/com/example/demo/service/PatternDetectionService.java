package com.example.demo.service;

import com.example.demo.dto.PatternDetectionResultDTO;
import java.util.List;

public interface PatternDetectionService {

    PatternDetectionResultDTO detectPattern(Long zoneId);

    List<PatternDetectionResultDTO> getResultsByZone(Long zoneId);
}
