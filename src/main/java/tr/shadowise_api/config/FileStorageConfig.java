package tr.shadowise_api.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class FileStorageConfig {

    @Value("${file.storage.path:file-storage}")
    private String fileStoragePath;

    @PostConstruct
    public void init() {
        try {
            Path path = Paths.get(fileStoragePath);
            if (!Files.exists(path)) {
                Files.createDirectories(path);
                System.out.println("Created file storage directory: " + path.toAbsolutePath());
            } else {
                System.out.println("File storage directory already exists: " + path.toAbsolutePath());
            }
        } catch (Exception e) {
            throw new RuntimeException("Could not initialize file storage directory", e);
        }
    }
}