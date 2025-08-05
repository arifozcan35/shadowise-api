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

    /**
     * Soft delete quiz
     */
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

    /**
     * Hard delete quiz
     */
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
    
    /**
     * Check if quiz exists
     */
    public boolean quizExists(String id) {
        return quizRepository.existsById(id);
    }

    /**
     * Get quiz count
     */
    public long getQuizCount() {
        return quizRepository.count();
    }

    /**
     * Generate quiz questions from uploaded file
     */
    public IDataResult<Quiz> generateQuestionsFromFile(GenerateProjectQuestionsRequestDto requestDto, String userId) {
        try {
            // Get file information
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
            
            // Create a new quiz with the generated questions
            Quiz quiz = new Quiz();
            String fileName = uploadedFileService.getOriginalFileName(requestDto.getFileId());
            quiz.setName("Generated Quiz for " + fileName);
            quiz.setDescription("Automatically generated quiz from uploaded file");
            quiz.setLevel(QuizLevel.Intermediate); // Default level, can be changed later
            quiz.setTimeLimit(0); // No time limit by default
            quiz.setBestScore(0.0); // No score yet
            quiz.setQuestions(responseDto.getQuestions().toArray(new Question[0]));
            quiz.setLastAttempt(new Date());
            quiz.setUserId(userId);
            
            // Save the quiz to database
            Quiz savedQuiz = quizRepository.save(quiz);
            return new SuccessDataResult<>(savedQuiz, "Quiz generated successfully");
        } catch (Exception e) {
            return new ErrorDataResult<>(null, "Failed to generate quiz: " + e.getMessage());
        }
    }

    /**
     * Get quizzes for a specific user
     */
    public IDataResult<List<Quiz>> getQuizzesByUserId(String userId) {
        try {
            List<Quiz> quizzes = quizRepository.findByUserId(userId);
            return new SuccessDataResult<>(quizzes, "User quizzes retrieved successfully");
        } catch (Exception e) {
            return new ErrorDataResult<>(null, "Failed to retrieve user quizzes: " + e.getMessage());
        }
    }
} 