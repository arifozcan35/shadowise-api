package tr.shadowise_api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tr.shadowise_api.entity.UploadedFile;
import tr.shadowise_api.service.UploadedFileService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/files")
public class UploadedFileController {
    private final UploadedFileService uploadedFileService;

    @GetMapping
    public List<UploadedFile> getAllFiles() {
        return uploadedFileService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UploadedFile> getFileById(@PathVariable String id) {
        return uploadedFileService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UploadedFile createFile(@RequestBody UploadedFile uploadedFile) {
        return uploadedFileService.create(uploadedFile);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UploadedFile> updateFile(@PathVariable String id, @RequestBody UploadedFile uploadedFile) {
        try {
            UploadedFile updatedFile = uploadedFileService.update(id, uploadedFile);
            return ResponseEntity.ok(updatedFile);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFile(@PathVariable String id) {
        try {
            uploadedFileService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
} 