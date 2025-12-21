package com.example.demo.service;

import com.example.demo.model.AnalysisLog;
import java.util.List;

public interface AnalysisLogService {
    AnalysisLog addLog(Long zoneId, String message) throws Exception;
    List<AnalysisLog> getLogsByZone(Long zoneId);
}