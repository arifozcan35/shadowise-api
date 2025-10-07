package tr.shadowise_api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import tr.shadowise_api.core.response.IDataResult;
import tr.shadowise_api.core.response.IResult;
import tr.shadowise_api.dto.request.GenerateProjectQuestionsRequestDto;
import tr.shadowise_api.entity.Quiz;
import tr.shadowise_api.repository.UserRepository;
import tr.shadowise_api.service.QuizService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/quizzes")
public class QuizController {
    private final QuizService quizService;
    private final UserRepository userRepository;

    @GetMapping
    public ResponseEntity<?> getAllQuizzes() {
        IDataResult<List<Quiz>> result = quizService.getAll();
        if (result.isSuccess()) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getQuizById(@PathVariable String id) {
        IDataResult<Quiz> result = quizService.getById(id);
        if (result.isSuccess()) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        }
    }

    @PostMapping
    public ResponseEntity<?> createQuiz(@RequestBody Quiz quiz) {
        IDataResult<Quiz> result = quizService.create(quiz);
        if (result.isSuccess()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateQuiz(@PathVariable String id, @RequestBody Quiz quiz) {
        IDataResult<Quiz> result = quizService.update(id, quiz);
        if (result.isSuccess()) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteQuiz(@PathVariable String id) {
        IResult result = quizService.deleteQuiz(id);
        if (result.isSuccess()) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        }
    }

    @DeleteMapping("/{id}/soft")
    public ResponseEntity<?> softDeleteQuiz(@PathVariable String id) {
        IResult result = quizService.softDeleteQuiz(id);
        if (result.isSuccess()) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        }
    }

    @GetMapping("/count")
    public ResponseEntity<?> getQuizCount() {
        long count = quizService.getQuizCount();
        return ResponseEntity.ok("Total quizzes: " + count);
    }

    @GetMapping("/exists/{id}")
    public ResponseEntity<?> checkQuizExists(@PathVariable String id) {
        boolean exists = quizService.quizExists(id);
        return ResponseEntity.ok("Quiz exists: " + exists);
    }
    
    @PostMapping("/generate-questions")
    public ResponseEntity<?> generateQuestions(@RequestBody GenerateProjectQuestionsRequestDto requestDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userDetails = (User) authentication.getPrincipal();
        String username = userDetails.getUsername();
        String userId = userRepository.findByUsername(username).orElseThrow().getId();
        
        IDataResult<Quiz> result = quizService.generateQuestionsFromFile(requestDto, userId);
        if (result.isSuccess()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
    }
    
    @GetMapping("/user-quizzes/file/{fileId}")
    public ResponseEntity<?> getUserQuizzesByFileId(@PathVariable String fileId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userDetails = (User) authentication.getPrincipal();
        String username = userDetails.getUsername();
        String userId = userRepository.findByUsername(username).orElseThrow().getId();
        
        IDataResult<List<Quiz>> result = quizService.getQuizzesByUserIdAndFileId(userId, fileId);
        if (result.isSuccess()) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }

    @GetMapping("/user-quizzes/project/{projectId}")
    public ResponseEntity<?> getUserQuizzesByProjectId(@PathVariable String projectId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userDetails = (User) authentication.getPrincipal();
        String username = userDetails.getUsername();
        String userId = userRepository.findByUsername(username).orElseThrow().getId();
        
        IDataResult<List<Quiz>> result = quizService.getQuizzesByUserIdAndProjectId(userId, projectId);
        if (result.isSuccess()) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }
} 