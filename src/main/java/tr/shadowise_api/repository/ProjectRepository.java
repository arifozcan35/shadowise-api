package tr.shadowise_api.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import tr.shadowise_api.entity.Project;

public interface ProjectRepository extends MongoRepository<Project, String> {
}
