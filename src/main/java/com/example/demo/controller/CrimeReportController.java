package com.example.demo.controller;

import com.example.demo.model.CrimeReport;
import com.example.demo.service.CrimeReportService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/crimes")
public class CrimeReportController {

    private final CrimeReportService crimeReportService;

    public CrimeReportController(CrimeReportService crimeReportService) {
        this.crimeReportService = crimeReportService;
    }

    @PostMapping
    public CrimeReport add(@RequestBody CrimeReport report) {
        return crimeReportService.addReport(report);
    }

    @GetMapping
    public List<CrimeReport> list() {
        return crimeReportService.getAllReports();
    }
}
