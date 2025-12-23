import com.example.demo.dto.PatternDetectionResultDTO;
import java.util.List;

public interface PatternDetectionService {
    PatternDetectionResultDTO detectPattern(Long zoneId) throws Exception;
    List<PatternDetectionResultDTO> getResultsByZone(Long zoneId);
}
