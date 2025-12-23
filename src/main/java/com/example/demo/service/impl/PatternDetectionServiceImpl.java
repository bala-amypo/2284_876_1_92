import com.example.demo.dto.PatternDetectionResultDTO;

@Override
public PatternDetectionResultDTO detectPattern(Long zoneId) throws Exception {

    HotspotZone zone = zoneRepo.findById(zoneId)
            .orElseThrow(() -> new RuntimeException("Zone not found"));

    double minLat = zone.getCenterLat() - 0.1;
    double maxLat = zone.getCenterLat() + 0.1;
    double minLong = zone.getCenterLong() - 0.1;
    double maxLong = zone.getCenterLong() + 0.1;

    List<CrimeReport> crimes =
            reportRepo.findByLatLongRange(minLat, maxLat, minLong, maxLong);

    int crimeCount = crimes.size();

    String detectedPattern;
    String newSeverity;

    if (crimeCount > 5) {
        detectedPattern = "High Risk Pattern Detected";
        newSeverity = "HIGH";
    } else if (crimeCount > 0) {
        detectedPattern = "Medium Risk Pattern Detected";
        newSeverity = "MEDIUM";
    } else {
        detectedPattern = "No Pattern Detected";
        newSeverity = "LOW";
    }

    PatternDetectionResult result =
            new PatternDetectionResult(zone, LocalDate.now(), crimeCount, detectedPattern);

    result = resultRepo.save(result);

    zone.setSeverityLevel(newSeverity);
    zoneRepo.save(zone);

    return new PatternDetectionResultDTO(
            result.getId(),
            zone.getId(),
            result.getAnalysisDate(),
            result.getCrimeCount(),
            result.getDetectedPattern()
    );
}
