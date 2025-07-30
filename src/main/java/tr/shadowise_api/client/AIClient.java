package tr.shadowise_api.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tr.shadowise_api.entity.FlashCard;
import tr.shadowise_api.entity.Question;
import tr.shadowise_api.entity.Note;

import java.util.List;
import java.util.Map;

@FeignClient(
    name = "ai-api", 
    url = "${ai.api.url}",
    configuration = tr.shadowise_api.config.FeignClientConfig.class
)
public interface AIClient {

    @GetMapping("/api/health")
    Map<String, Object> healthCheck();
    
    @PostMapping(value = "/api/upload-pdf", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    Map<String, Object> uploadPdf(@RequestPart("file") MultipartFile file);
    
    @PostMapping("/api/generate-summary")
    Note generateSummary(@RequestBody Map<String, Object> request);
    
    @PostMapping("/api/generate-questions")
    List<Question> generateQuestions(@RequestBody Map<String, Object> request);
    
    @PostMapping("/api/generate-flashcards")
    FlashCard generateFlashcards(@RequestBody Map<String, Object> request);
}
