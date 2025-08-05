package tr.shadowise_api.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import tr.shadowise_api.entity.Quiz;

import java.util.List;

@Repository
public interface QuizRepository extends MongoRepository<Quiz, String> {
    List<Quiz> findByUserId(String userId);
} 