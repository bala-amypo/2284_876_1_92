package com.example.demo.controller;

import com.example.demo.model.CrimeReport;
import com.example.demo.service.CrimeReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reports")
public class CrimeReportController {

    private final CrimeReportService crimeReportService;

    public CrimeReportController(CrimeReportService crimeReportService) {
        this.crimeReportService = crimeReportService;
    }

    @PostMapping
    public ResponseEntity<CrimeReport> addReport(@RequestBody CrimeReport report) {
        return ResponseEntity.ok(crimeReportService.addReport(report));
    }

    @GetMapping
    public ResponseEntity<List<CrimeReport>> getAllReports() {
        return ResponseEntity.ok(crimeReportService.getAllReports());
    }
}
