package tr.shadowise_api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tr.shadowise_api.entity.UploadedFile;
import tr.shadowise_api.repository.UploadedFileRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UploadedFileService {
    private final UploadedFileRepository uploadedFileRepository;

    public List<UploadedFile> getAll() {
        return uploadedFileRepository.findAll();
    }

    public Optional<UploadedFile> getById(String id) {
        return uploadedFileRepository.findById(id);
    }

    public UploadedFile create(UploadedFile uploadedFile) {
        return uploadedFileRepository.save(uploadedFile);
    }

    public UploadedFile update(String id, UploadedFile newUploadedFile) {
        UploadedFile uploadedFile = uploadedFileRepository.findById(id).orElseThrow();
        uploadedFile.setFileName(newUploadedFile.getFileName());
        uploadedFile.setFileType(newUploadedFile.getFileType());
        uploadedFile.setFilePath(newUploadedFile.getFilePath());
        uploadedFile.setProject(newUploadedFile.getProject());
        return uploadedFileRepository.save(uploadedFile);
    }

    public void delete(String id) {
        uploadedFileRepository.deleteById(id);
    }
} 