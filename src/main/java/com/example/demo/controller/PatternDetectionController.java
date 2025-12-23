import com.example.demo.dto.PatternDetectionResultDTO;

@RestController
@RequestMapping("/patterns")
public class PatternDetectionController {

    private final PatternDetectionService patternDetectionService;

    public PatternDetectionController(PatternDetectionService patternDetectionService) {
        this.patternDetectionService = patternDetectionService;
    }

    @PostMapping("/detect/{zoneId}")
    public ResponseEntity<PatternDetectionResultDTO> detectPattern(
            @PathVariable Long zoneId) throws Exception {

        return ResponseEntity.ok(
                patternDetectionService.detectPattern(zoneId)
        );
    }

    @GetMapping("/zone/{zoneId}")
    public ResponseEntity<List<PatternDetectionResultDTO>> getResultsByZone(
            @PathVariable Long zoneId) {

        return ResponseEntity.ok(
                patternDetectionService.getResultsByZone(zoneId)
        );
    }
}
