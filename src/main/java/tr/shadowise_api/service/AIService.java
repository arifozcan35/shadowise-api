package tr.shadowise_api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tr.shadowise_api.client.AIClient;
import tr.shadowise_api.core.response.IDataResult;
import tr.shadowise_api.core.response.SuccessDataResult;
import tr.shadowise_api.dto.response.GenerateQuestionsResponseDto;
import tr.shadowise_api.dto.response.GenerateFlashcardsResponseDto;
import tr.shadowise_api.dto.response.GenerateFlashcardsResponseDto.FlashcardPair;

import tr.shadowise_api.entity.Question;

import java.util.ArrayList;
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

    public IDataResult<String> generateSummary(String cleaned_file_path, Integer max_words, Double temperature) {
        Map<String, Object> request = Map.of(
            "cleaned_file_path", cleaned_file_path,
            "max_words", max_words,
            "temperature", temperature
        );
        String result = aiClient.generateSummary(request);
        return new SuccessDataResult<>(result, "Summary generated successfully");
    }

    public IDataResult<GenerateQuestionsResponseDto> generateQuestions(String cleaned_file_path, int num_questions) {
        Map<String, Object> request = Map.of(
            "cleaned_file_path", cleaned_file_path,
            "num_questions", num_questions
        );
        
        Map<String, Object> response = aiClient.generateQuestions(request);
        GenerateQuestionsResponseDto result = new GenerateQuestionsResponseDto();
        
        if (response != null && response.containsKey("data")) {
            Map<String, Object> data = (Map<String, Object>) response.get("data");
            
            if (data.containsKey("questions")) {
                List<Map<String, Object>> questionsMap = (List<Map<String, Object>>) data.get("questions");
                List<Question> questions = new ArrayList<>();
                
                for (Map<String, Object> questionMap : questionsMap) {
                    Question question = new Question();
                    question.setQuestionText((String) questionMap.get("questionText"));
                    
                    @SuppressWarnings("unchecked")
                    List<String> choices = (List<String>) questionMap.get("choices");
                    question.setCorrectAnswerIndex(((Number) questionMap.get("correctAnswerIndex")).intValue());
                    
                    // Convert choices to Choice entities
                    question.setChoices(choices);
                    
                    questions.add(question);
                }
                
                result.setQuestions(questions);
            }
            
            if (data.containsKey("num_questions")) {
                result.setNum_questions(((Number) data.get("num_questions")).intValue());
            }
            
            if (data.containsKey("file_path")) {
                result.setFile_path((String) data.get("file_path"));
            }
        }
        
        String message = "Questions generated successfully";
        if (response != null && response.containsKey("message")) {
            message = (String) response.get("message");
        }
        
        return new SuccessDataResult<>(result, message);
    }

    public IDataResult<GenerateFlashcardsResponseDto> generateFlashcards(String cleaned_file_path, int num_pairs) {
        Map<String, Object> request = Map.of(
            "cleaned_file_path", cleaned_file_path,
            "num_pairs", num_pairs
        );
        
        Map<String, Object> response = aiClient.generateFlashcards(request);
        GenerateFlashcardsResponseDto result = new GenerateFlashcardsResponseDto();
        
        if (response != null && response.containsKey("data")) {
            Map<String, Object> data = (Map<String, Object>) response.get("data");
            
            if (data.containsKey("flashcards")) {
                List<Map<String, Object>> flashcardsMap = (List<Map<String, Object>>) data.get("flashcards");
                List<FlashcardPair> flashcards = new ArrayList<>();
                
                for (Map<String, Object> flashcardMap : flashcardsMap) {
                    FlashcardPair flashcard = new FlashcardPair();
                    flashcard.setQuestion((String) flashcardMap.get("question"));
                    flashcard.setAnswer((String) flashcardMap.get("answer"));
                    flashcards.add(flashcard);
                }
                
                result.setFlashcards(flashcards);
            }
            
            if (data.containsKey("num_pairs")) {
                result.setNum_pairs(((Number) data.get("num_pairs")).intValue());
            }
            
            if (data.containsKey("file_path")) {
                result.setFile_path((String) data.get("file_path"));
            }
        }
        
        String message = num_pairs + " adet flashcard başarıyla oluşturuldu";
        if (response != null && response.containsKey("message")) {
            message = (String) response.get("message");
        }
        
        return new SuccessDataResult<>(result, message);
    }
} 