package com.example.demo.service;

import com.example.demo.model.CrimeReport;
import java.util.List;

public interface CrimeReportService {
    CrimeReport addReport(CrimeReport report) throws Exception;
    List<CrimeReport> getAllReports();
}