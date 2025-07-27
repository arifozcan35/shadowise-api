package tr.shadowise_api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tr.shadowise_api.entity.Quiz;
import tr.shadowise_api.repository.QuizRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuizService {
    private final QuizRepository quizRepository;

    public List<Quiz> getAll() {
        return quizRepository.findAll();
    }

    public Optional<Quiz> getById(String id) {
        return quizRepository.findById(id);
    }

    public Quiz create(Quiz quiz) {
        return quizRepository.save(quiz);
    }

    public Quiz update(String id, Quiz newQuiz) {
        Quiz quiz = quizRepository.findById(id).orElseThrow();
        quiz.setName(newQuiz.getName());
        quiz.setDescription(newQuiz.getDescription());
        quiz.setLevel(newQuiz.getLevel());
        quiz.setTimeLimit(newQuiz.getTimeLimit());
        quiz.setBestScore(newQuiz.getBestScore());
        quiz.setQuestions(newQuiz.getQuestions());
        quiz.setLastAttempt(newQuiz.getLastAttempt());
        return quizRepository.save(quiz);
    }

    public void delete(String id) {
        quizRepository.deleteById(id);
    }
} 