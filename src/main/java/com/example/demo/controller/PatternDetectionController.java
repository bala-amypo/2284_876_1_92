package com.example.demo.controller;

import com.example.demo.model.PatternDetectionResult;
import com.example.demo.service.PatternDetectionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/patterns")
@Tag(name = "Pattern Detection")
public class PatternDetectionController {
    
    private final PatternDetectionService patternDetectionService;
    
    public PatternDetectionController(PatternDetectionService patternDetectionService) {
        this.patternDetectionService = patternDetectionService;
    }
    
    @PostMapping("/detect/{zoneId}")
    @Operation(summary = "Run pattern detection for zone")
    public ResponseEntity<PatternDetectionResult> detectPattern(@PathVariable Long zoneId) {
        try {
            PatternDetectionResult result = patternDetectionService.detectPattern(zoneId);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
    
    @GetMapping("/zone/{zoneId}")
    @Operation(summary = "Get detection results for zone")
    public ResponseEntity<List<PatternDetectionResult>> getResultsByZone(@PathVariable Long zoneId) {
        List<PatternDetectionResult> results = patternDetectionService.getResultsByZone(zoneId);
        return ResponseEntity.ok(results);
    }
}