private PatternDetectionResultDTO mapToDTO(PatternDetectionResult result) {
    return new PatternDetectionResultDTO(
            result.getId(),
            result.getZone().getId(), // ✅ Long
            result.getAnalysisDate(),
            result.getCrimeCount(),
            result.getDetectedPattern()
    );
}
