package tr.shadowise_api.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import tr.shadowise_api.entity.UploadedFile;

@Repository
public interface UploadedFileRepository extends MongoRepository<UploadedFile, String> {

} 