package com.example.demo.service;

import com.example.demo.model.CrimeReport;
import java.util.List;

public interface CrimeReportService {
    CrimeReport addReport(CrimeReport report);
    List<CrimeReport> getAllReports();
}
