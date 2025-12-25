package com.example.demo.service;

import com.example.demo.model.AnalysisLog;
import java.util.List;

public interface AnalysisLogService {

    AnalysisLog save(AnalysisLog log);

    List<AnalysisLog> getByZone(Long zoneId);
}
