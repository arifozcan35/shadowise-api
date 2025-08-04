package tr.shadowise_api.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Component
public class FileUtils {

    @Value("${file.storage.path:file-storage}")
    private String fileStoragePath;

    /**
     * Store file in the file system and return the path
     */
    public String storeFile(MultipartFile file) throws IOException {
        // Normalize file name
        String originalFileName = file.getOriginalFilename();
        String fileExtension = "";
        if (originalFileName != null && originalFileName.contains(".")) {
            fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
        }
        
        // Generate unique file name
        String fileName = UUID.randomUUID().toString() + fileExtension;
        
        // Create the storage path if it doesn't exist
        Path storageLocation = Paths.get(fileStoragePath).toAbsolutePath().normalize();
        if (!Files.exists(storageLocation)) {
            Files.createDirectories(storageLocation);
        }
        
        // Copy the file to the target location
        Path targetPath = storageLocation.resolve(fileName);
        Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
        
        return targetPath.toString();
    }
}