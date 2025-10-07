package tr.shadowise_api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import tr.shadowise_api.core.response.*;
import tr.shadowise_api.entity.UploadedFile;
import tr.shadowise_api.repository.UploadedFileRepository;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UploadedFileService {
    private final UploadedFileRepository uploadedFileRepository;

    public IDataResult<List<UploadedFile>> getAll() {
        try {
            List<UploadedFile> files = uploadedFileRepository.findAll();
            return new SuccessDataResult<>(files, "Files retrieved successfully");
        } catch (Exception e) {
            return new ErrorDataResult<>(null, "Failed to retrieve files: " + e.getMessage());
        }
    }

    public IDataResult<UploadedFile> getById(String id) {
        try {
            Optional<UploadedFile> file = uploadedFileRepository.findById(id);
            if (file.isPresent()) {
                return new SuccessDataResult<>(file.get(), "File retrieved successfully");
            } else {
                return new ErrorDataResult<>(null, "File not found with id: " + id);
            }
        } catch (Exception e) {
            return new ErrorDataResult<>(null, "Failed to retrieve file: " + e.getMessage());
        }
    }

    public IDataResult<UploadedFile> create(UploadedFile uploadedFile) {
        try {
            uploadedFile.setCreatedAt(LocalDateTime.now());
            uploadedFile.setUpdatedAt(LocalDateTime.now());
            UploadedFile savedFile = uploadedFileRepository.save(uploadedFile);
            return new SuccessDataResult<>(savedFile, "File created successfully");
        } catch (Exception e) {
            return new ErrorDataResult<>(null, "Failed to create file: " + e.getMessage());
        }
    }

    public IDataResult<UploadedFile> update(String id, UploadedFile newUploadedFile) {
        try {
            Optional<UploadedFile> existingFile = uploadedFileRepository.findById(id);
            if (existingFile.isPresent()) {
                UploadedFile uploadedFile = existingFile.get();
                uploadedFile.setFileName(newUploadedFile.getFileName());
                uploadedFile.setFileType(newUploadedFile.getFileType());
                uploadedFile.setFilePath(newUploadedFile.getFilePath());
                uploadedFile.setApiFilePath(newUploadedFile.getApiFilePath());
                uploadedFile.setProjectId(newUploadedFile.getProjectId());
                uploadedFile.setUpdatedAt(LocalDateTime.now());
                
                UploadedFile updatedFile = uploadedFileRepository.save(uploadedFile);
                return new SuccessDataResult<>(updatedFile, "File updated successfully");
            } else {
                return new ErrorDataResult<>(null, "File not found with id: " + id);
            }
        } catch (Exception e) {
            return new ErrorDataResult<>(null, "Failed to update file: " + e.getMessage());
        }
    }

    public IResult softDeleteFile(String id) {
        try {
            Optional<UploadedFile> existingFile = uploadedFileRepository.findById(id);
            if (existingFile.isPresent()) {
                UploadedFile uploadedFile = existingFile.get();
                uploadedFile.setDeletedAt(LocalDateTime.now());
                uploadedFile.setUpdatedAt(LocalDateTime.now());
                
                uploadedFileRepository.save(uploadedFile);
                return new SuccessResult("File soft deleted successfully");
            } else {
                return new ErrorResult("File not found with id: " + id);
            }
        } catch (Exception e) {
            return new ErrorResult("Failed to soft delete file: " + e.getMessage());
        }
    }

    public IResult deleteFile(String id) {
        try {
            if (uploadedFileRepository.existsById(id)) {
                uploadedFileRepository.deleteById(id);
                return new SuccessResult("File deleted successfully");
            } else {
                return new ErrorResult("File not found with id: " + id);
            }
        } catch (Exception e) {
            return new ErrorResult("Failed to delete file: " + e.getMessage());
        }
    }
    
    public boolean fileExists(String id) {
        return uploadedFileRepository.existsById(id);
    }

    public long getFileCount() {
        return uploadedFileRepository.count();
    }
    
    public IDataResult<Resource> loadFileAsResource(String id) {
        try {
            Optional<UploadedFile> fileOptional = uploadedFileRepository.findById(id);
            if (!fileOptional.isPresent()) {
                return new ErrorDataResult<>(null, "File not found with id: " + id);
            }
            
            UploadedFile file = fileOptional.get();
            String filePath = file.getFilePath();
            
            try {
                Path path = Paths.get(filePath);
                Resource resource = new UrlResource(path.toUri());
                
                if (resource.exists()) {
                    return new SuccessDataResult<>(resource, "File loaded successfully");
                } else {
                    return new ErrorDataResult<>(null, "File not found at path: " + filePath);
                }
            } catch (MalformedURLException ex) {
                return new ErrorDataResult<>(null, "Error loading file: " + ex.getMessage());
            }
        } catch (Exception e) {
            return new ErrorDataResult<>(null, "Failed to load file: " + e.getMessage());
        }
    }
    
    public String getOriginalFileName(String id) {
        Optional<UploadedFile> fileOptional = uploadedFileRepository.findById(id);
        return fileOptional.map(UploadedFile::getFileName).orElse("unknown");
    }
    
    public String getApiFilePath(String id) {
        Optional<UploadedFile> fileOptional = uploadedFileRepository.findById(id);
        return fileOptional.map(UploadedFile::getApiFilePath).orElse(null);
    }
        
    public String getProjectId(String fileId) {
        Optional<UploadedFile> fileOptional = uploadedFileRepository.findById(fileId);
        return fileOptional.map(UploadedFile::getProjectId).orElse(null);
    }
} 