package tr.shadowise_api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tr.shadowise_api.core.response.*;
import tr.shadowise_api.entity.UploadedFile;
import tr.shadowise_api.repository.UploadedFileRepository;

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
                uploadedFile.setProject(newUploadedFile.getProject());
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

    /**
     * Soft delete file
     */
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

    /**
     * Hard delete file
     */
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
    
    /**
     * Check if file exists
     */
    public boolean fileExists(String id) {
        return uploadedFileRepository.existsById(id);
    }

    /**
     * Get file count
     */
    public long getFileCount() {
        return uploadedFileRepository.count();
    }
} 