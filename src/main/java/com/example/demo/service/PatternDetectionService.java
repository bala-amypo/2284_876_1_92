public interface PatternDetectionService {
    PatternDetectionResultDTO detectPattern(Long zoneId);
    List<PatternDetectionResultDTO> getResultsByZone(Long zoneId);
}
