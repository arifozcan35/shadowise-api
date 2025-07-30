package tr.shadowise_api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tr.shadowise_api.client.AIClient;
import tr.shadowise_api.core.response.IDataResult;
import tr.shadowise_api.core.response.SuccessDataResult;
import tr.shadowise_api.entity.FlashCard;
import tr.shadowise_api.entity.Question;
import tr.shadowise_api.entity.Note;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AIService {

    private final AIClient aiClient;

    public IDataResult<Map<String, Object>> healthCheck() {
        Map<String, Object> result = aiClient.healthCheck();
        return new SuccessDataResult<>(result, "AI service health check completed");
    }

    public IDataResult<Map<String, Object>> uploadPdf(MultipartFile file) {
        Map<String, Object> result = aiClient.uploadPdf(file);
        return new SuccessDataResult<>(result, "PDF uploaded successfully");
    }

    public IDataResult<Note> generateSummary(String content) {
        Map<String, Object> request = Map.of(
            "content", content
        );
        Note result = aiClient.generateSummary(request);
        return new SuccessDataResult<>(result, "Summary generated successfully");
    }

    public IDataResult<Note> generateSummaryFromProject(String projectId) {
        Map<String, Object> request = Map.of(
            "project_id", projectId
        );
        Note result = aiClient.generateSummary(request);
        return new SuccessDataResult<>(result, "Summary generated successfully from project");
    }

    public IDataResult<List<Question>> generateQuestions(String content, int questionCount, String difficulty) {
        Map<String, Object> request = Map.of(
            "content", content,
            "question_count", questionCount,
            "difficulty", difficulty
        );
        List<Question> result = aiClient.generateQuestions(request);
        return new SuccessDataResult<>(result, "Questions generated successfully");
    }

    public IDataResult<List<Question>> generateQuestionsFromProject(String projectId, int questionCount, String difficulty) {
        Map<String, Object> request = Map.of(
            "project_id", projectId,
            "question_count", questionCount,
            "difficulty", difficulty
        );
        List<Question> result = aiClient.generateQuestions(request);
        return new SuccessDataResult<>(result, "Questions generated successfully from project");
    }

    public IDataResult<FlashCard> generateFlashcards(String content, int cardCount) {
        Map<String, Object> request = Map.of(
            "content", content,
            "card_count", cardCount
        );
        FlashCard result = aiClient.generateFlashcards(request);
        return new SuccessDataResult<>(result, "Flashcards generated successfully");
    }

    public IDataResult<FlashCard> generateFlashcardsFromProject(String projectId, int cardCount) {
        Map<String, Object> request = Map.of(
            "project_id", projectId,
            "card_count", cardCount
        );
        FlashCard result = aiClient.generateFlashcards(request);
        return new SuccessDataResult<>(result, "Flashcards generated successfully from project");
    }
} 