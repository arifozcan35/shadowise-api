package tr.shadowise_api.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    
    @PostMapping(value = "/api/generate-summary", produces = "text/markdown;charset=utf-8")
    String generateSummary(@RequestBody Map<String, Object> request);
    
    @PostMapping("/api/generate-questions")
    Map<String, Object> generateQuestions(@RequestBody Map<String, Object> request);
    
    @PostMapping("/api/generate-flashcards")
    Map<String, Object> generateFlashcards(@RequestBody Map<String, Object> request);
}
