package com.example.demo.service.impl;

import com.example.demo.model.AnalysisLog;
import com.example.demo.repository.AnalysisLogRepository;
import com.example.demo.service.AnalysisLogService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnalysisLogServiceImpl implements AnalysisLogService {

    private final AnalysisLogRepository logRepo;

    public AnalysisLogServiceImpl(AnalysisLogRepository logRepo) {
        this.logRepo = logRepo;
    }

    @Override
    public AnalysisLog save(AnalysisLog log) {
        return logRepo.save(log);
    }

    @Override
    public List<AnalysisLog> getByZone(Long zoneId) {
        return logRepo.findByZone_Id(zoneId);
    }
}
