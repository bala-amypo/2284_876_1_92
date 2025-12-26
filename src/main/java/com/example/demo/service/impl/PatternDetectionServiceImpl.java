public PatternDetectionServiceImpl(
        HotspotZoneRepository hotspotZoneRepository,
        CrimeReportRepository crimeReportRepository,
        PatternDetectionResultRepository patternDetectionResultRepository,
        AnalysisLogRepository analysisLogRepository) {

    this.hotspotZoneRepository = hotspotZoneRepository;
    this.crimeReportRepository = crimeReportRepository;
    this.patternDetectionResultRepository = patternDetectionResultRepository;
    this.analysisLogRepository = analysisLogRepository;
}
