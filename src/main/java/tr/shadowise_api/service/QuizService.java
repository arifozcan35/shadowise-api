package tr.shadowise_api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tr.shadowise_api.core.response.*;
import tr.shadowise_api.dto.request.GenerateProjectQuestionsRequestDto;
import tr.shadowise_api.dto.response.GenerateQuestionsResponseDto;
import tr.shadowise_api.entity.Question;
import tr.shadowise_api.entity.Quiz;
import tr.shadowise_api.entity.enums.QuizLevel;
import tr.shadowise_api.repository.QuizRepository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuizService {
    private final AIService aiService;
    private final UploadedFileService uploadedFileService;
    private final QuizRepository quizRepository;

    public IDataResult<List<Quiz>> getAll() {
        try {
            List<Quiz> quizzes = quizRepository.findAll();
            return new SuccessDataResult<>(quizzes, "Quizzes retrieved successfully");
        } catch (Exception e) {
            return new ErrorDataResult<>(null, "Failed to retrieve quizzes: " + e.getMessage());
        }
    }

    public IDataResult<Quiz> getById(String id) {
        try {
            Optional<Quiz> quiz = quizRepository.findById(id);
            if (quiz.isPresent()) {
                return new SuccessDataResult<>(quiz.get(), "Quiz retrieved successfully");
            } else {
                return new ErrorDataResult<>(null, "Quiz not found with id: " + id);
            }
        } catch (Exception e) {
            return new ErrorDataResult<>(null, "Failed to retrieve quiz: " + e.getMessage());
        }
    }

    public IDataResult<Quiz> create(Quiz quiz) {
        try {
            // Set timestamps if not already set
            LocalDateTime now = LocalDateTime.now();
            if (quiz.getCreatedAt() == null) {
                quiz.setCreatedAt(now);
            }
            if (quiz.getUpdatedAt() == null) {
                quiz.setUpdatedAt(now);
            }
            
            Quiz savedQuiz = quizRepository.save(quiz);
            return new SuccessDataResult<>(savedQuiz, "Quiz created successfully");
        } catch (Exception e) {
            return new ErrorDataResult<>(null, "Failed to create quiz: " + e.getMessage());
        }
    }

    public IDataResult<Quiz> update(String id, Quiz newQuiz) {
        try {
            Optional<Quiz> existingQuiz = quizRepository.findById(id);
            if (existingQuiz.isPresent()) {
                Quiz quiz = existingQuiz.get();
                quiz.setName(newQuiz.getName());
                quiz.setDescription(newQuiz.getDescription());
                quiz.setLevel(newQuiz.getLevel());
                quiz.setTimeLimit(newQuiz.getTimeLimit());
                quiz.setBestScore(newQuiz.getBestScore());
                quiz.setQuestions(newQuiz.getQuestions());
                quiz.setLastAttempt(newQuiz.getLastAttempt());
                quiz.setUpdatedAt(LocalDateTime.now());
                
                Quiz updatedQuiz = quizRepository.save(quiz);
                return new SuccessDataResult<>(updatedQuiz, "Quiz updated successfully");
            } else {
                return new ErrorDataResult<>(null, "Quiz not found with id: " + id);
            }
        } catch (Exception e) {
            return new ErrorDataResult<>(null, "Failed to update quiz: " + e.getMessage());
        }
    }

    public IResult softDeleteQuiz(String id) {
        try {
            Optional<Quiz> existingQuiz = quizRepository.findById(id);
            if (existingQuiz.isPresent()) {
                Quiz quiz = existingQuiz.get();
                quiz.setDeletedAt(LocalDateTime.now());
                quiz.setUpdatedAt(LocalDateTime.now());
                
                quizRepository.save(quiz);
                return new SuccessResult("Quiz soft deleted successfully");
            } else {
                return new ErrorResult("Quiz not found with id: " + id);
            }
        } catch (Exception e) {
            return new ErrorResult("Failed to soft delete quiz: " + e.getMessage());
        }
    }

    public IResult deleteQuiz(String id) {
        try {
            if (quizRepository.existsById(id)) {
                quizRepository.deleteById(id);
                return new SuccessResult("Quiz deleted successfully");
            } else {
                return new ErrorResult("Quiz not found with id: " + id);
            }
        } catch (Exception e) {
            return new ErrorResult("Failed to delete quiz: " + e.getMessage());
        }
    }
    
    public boolean quizExists(String id) {
        return quizRepository.existsById(id);
    }

    public long getQuizCount() {
        return quizRepository.count();
    }

    public IDataResult<Quiz> generateQuestionsFromFile(GenerateProjectQuestionsRequestDto requestDto, String userId) {
        try {
            String apiFilePath = uploadedFileService.getApiFilePath(requestDto.getFileId());
            if (apiFilePath == null) {
                return new ErrorDataResult<>(null, "File not found with id: " + requestDto.getFileId());
            }
            
            IDataResult<GenerateQuestionsResponseDto> questionsResult = aiService.generateQuestions(
                    apiFilePath, 
                    requestDto.getNum_questions());
            
            if (!questionsResult.isSuccess()) {
                return new ErrorDataResult<>(null, "Failed to generate questions");
            }
            
            GenerateQuestionsResponseDto responseDto = questionsResult.getData();
            
            Quiz quiz = new Quiz();
            String fileName = uploadedFileService.getOriginalFileName(requestDto.getFileId());
            String projectId = uploadedFileService.getProjectId(requestDto.getFileId());
            
            quiz.setName("Generated Quiz for " + fileName);
            quiz.setDescription("Automatically generated quiz from uploaded file");
            quiz.setLevel(QuizLevel.Intermediate); 
            quiz.setTimeLimit(0); 
            quiz.setBestScore(0.0); 
            quiz.setQuestions(responseDto.getQuestions().toArray(new Question[0]));
            quiz.setLastAttempt(new Date());
            quiz.setUserId(userId);
            quiz.setFileId(requestDto.getFileId());
            quiz.setProjectId(projectId);
            
            LocalDateTime now = LocalDateTime.now();
            quiz.setCreatedAt(now);
            quiz.setUpdatedAt(now);
            
            Quiz savedQuiz = quizRepository.save(quiz);
            return new SuccessDataResult<>(savedQuiz, "Quiz generated successfully");
        } catch (Exception e) {
            return new ErrorDataResult<>(null, "Failed to generate quiz: " + e.getMessage());
        }
    }

    public IDataResult<List<Quiz>> getQuizzesByUserIdAndFileId(String userId, String fileId) {
        try {
            List<Quiz> quizzes = quizRepository.findByUserIdAndFileId(userId, fileId);
            return new SuccessDataResult<>(quizzes, "User file quizzes retrieved successfully");
        } catch (Exception e) {
            return new ErrorDataResult<>(null, "Failed to retrieve user file quizzes: " + e.getMessage());
        }
    }

    public IDataResult<List<Quiz>> getQuizzesByUserIdAndProjectId(String userId, String projectId) {
        try {
            List<Quiz> quizzes = quizRepository.findByUserIdAndProjectId(userId, projectId);
            return new SuccessDataResult<>(quizzes, "User project quizzes retrieved successfully");
        } catch (Exception e) {
            return new ErrorDataResult<>(null, "Failed to retrieve user project quizzes: " + e.getMessage());
        }
    }
} 