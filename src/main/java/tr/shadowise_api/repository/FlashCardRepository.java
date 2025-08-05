package tr.shadowise_api.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import tr.shadowise_api.entity.FlashCard;

import java.util.List;

@Repository
public interface FlashCardRepository extends MongoRepository<FlashCard, String> {
    List<FlashCard> findByFileId(String fileId);
    List<FlashCard> findByProjectId(String projectId);
} 