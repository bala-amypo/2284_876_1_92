package com.example.demo.service;

import com.example.demo.model.PatternDetectionResult;

import java.util.List;

public interface PatternDetectionService {

    PatternDetectionResult detectPattern(Long zoneId);

    List<PatternDetectionResult> getResultsByZone(Long zoneId);
}
