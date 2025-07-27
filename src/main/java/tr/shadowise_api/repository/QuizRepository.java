package tr.shadowise_api.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import tr.shadowise_api.entity.Quiz;

@Repository
public interface QuizRepository extends MongoRepository<Quiz, String> {
    // Custom query methods can be added here if needed
} 