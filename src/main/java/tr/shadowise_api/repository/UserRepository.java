package tr.shadowise_api.repository;

import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.data.mongodb.repository.MongoRepository;
import tr.shadowise_api.entity.User;

public interface UserRepository extends MongoRepository<User, String> {
}