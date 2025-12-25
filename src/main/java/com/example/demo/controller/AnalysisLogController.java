package com.example.demo.controller;

import com.example.demo.model.AnalysisLog;
import com.example.demo.service.AnalysisLogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/logs")
public class AnalysisLogController {

    private final AnalysisLogService analysisLogService;

    public AnalysisLogController(AnalysisLogService analysisLogService) {
        this.analysisLogService = analysisLogService;
    }

    @PostMapping("/{zoneId}")
    public ResponseEntity<AnalysisLog> addLog(
            @PathVariable Long zoneId,
            @RequestBody Map<String, String> body) {

        return ResponseEntity.ok(
                analysisLogService.addLog(zoneId, body.get("message"))
        );
    }

    @GetMapping("/zone/{zoneId}")
    public ResponseEntity<List<AnalysisLog>> getLogs(@PathVariable Long zoneId) {
        return ResponseEntity.ok(analysisLogService.getLogsByZone(zoneId));
    }
}
