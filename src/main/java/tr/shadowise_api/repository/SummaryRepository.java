package tr.shadowise_api.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import tr.shadowise_api.entity.Summary;

import java.util.List;
import java.util.Optional;

@Repository
public interface SummaryRepository extends MongoRepository<Summary, String> {
    Optional<Summary> findByFileId(String fileId);
    List<Summary> findByProjectId(String projectId);
    Optional<Summary> findByFileIdAndMaxWordsAndTemperature(String fileId, Integer maxWords, Double temperature);
    Optional<Summary> findByProjectIdAndFileIdAndMaxWordsAndTemperature(String projectId, String fileId, Integer maxWords, Double temperature);
}