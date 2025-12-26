package com.example.demo.service.impl;

import com.example.demo.model.CrimeReport;
import com.example.demo.repository.CrimeReportRepository;
import com.example.demo.service.CrimeReportService;

import java.time.LocalDateTime;
import java.util.List;

public class CrimeReportServiceImpl implements CrimeReportService {

    private final CrimeReportRepository repo;

    public CrimeReportServiceImpl(CrimeReportRepository repo) {
        this.repo = repo;
    }

    @Override
    public CrimeReport addReport(CrimeReport report) {

        if (report.getLatitude() == null || report.getLatitude() < -90 || report.getLatitude() > 90) {
            throw new RuntimeException("Invalid latitude");
        }
        if (report.getLongitude() == null || report.getLongitude() < -180 || report.getLongitude() > 180) {
            throw new RuntimeException("Invalid longitude");
        }

        if (report.getOccurredAt() == null) {
            report.setOccurredAt(LocalDateTime.now());
        }

        return repo.save(report);
    }

    @Override
    public List<CrimeReport> getAllReports() {
        return repo.findAll();
    }
}
