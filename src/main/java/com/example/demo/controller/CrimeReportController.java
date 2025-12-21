package com.example.demo.controller;

import com.example.demo.model.CrimeReport;
import com.example.demo.service.CrimeReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/reports")
@Tag(name = "Crime Reports")
public class CrimeReportController {
    
    private final CrimeReportService crimeReportService;
    
    public CrimeReportController(CrimeReportService crimeReportService) {
        this.crimeReportService = crimeReportService;
    }
    
    @PostMapping
    @Operation(summary = "Create crime report")
    public ResponseEntity<CrimeReport> addReport(@RequestBody CrimeReport report) {
        try {
            CrimeReport savedReport = crimeReportService.addReport(report);
            return ResponseEntity.status(201).body(savedReport);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
    
    @GetMapping
    @Operation(summary = "Get all crime reports")
    public ResponseEntity<List<CrimeReport>> getAllReports() {
        List<CrimeReport> reports = crimeReportService.getAllReports();
        return ResponseEntity.ok(reports);
    }
}