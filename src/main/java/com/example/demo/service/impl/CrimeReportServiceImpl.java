package com.example.demo.service.impl;

import com.example.demo.model.CrimeReport;
import com.example.demo.repository.CrimeReportRepository;
import com.example.demo.service.CrimeReportService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CrimeReportServiceImpl implements CrimeReportService {

    private final CrimeReportRepository crimeReportRepository;

    public CrimeReportServiceImpl(CrimeReportRepository crimeReportRepository) {
        this.crimeReportRepository = crimeReportRepository;
    }

    @Override
    public CrimeReport addReport(CrimeReport report) {

        if (report.getOccurredAt().isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("Occurred date cannot be in future");
        }

        if (report.getLatitude() < -90 || report.getLatitude() > 90) {
            throw new IllegalArgumentException("Invalid latitude");
        }

        if (report.getLongitude() < -180 || report.getLongitude() > 180) {
            throw new IllegalArgumentException("Invalid longitude");
        }

        return crimeReportRepository.save(report);
    }

    @Override
    public List<CrimeReport> getAllReports() {
        return crimeReportRepository.findAll();
    }
}
