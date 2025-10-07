package tr.shadowise_api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tr.shadowise_api.core.response.IDataResult;
import tr.shadowise_api.core.response.IResult;
import tr.shadowise_api.entity.UploadedFile;
import tr.shadowise_api.service.UploadedFileService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/files")
public class UploadedFileController {
    private final UploadedFileService uploadedFileService;

    @GetMapping
    public ResponseEntity<?> getAllFiles() {
        IDataResult<List<UploadedFile>> result = uploadedFileService.getAll();
        if (result.isSuccess()) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getFileById(@PathVariable String id) {
        IDataResult<UploadedFile> result = uploadedFileService.getById(id);
        if (result.isSuccess()) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        }
    }

    @PostMapping
    public ResponseEntity<?> createFile(@RequestBody UploadedFile uploadedFile) {
        IDataResult<UploadedFile> result = uploadedFileService.create(uploadedFile);
        if (result.isSuccess()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateFile(@PathVariable String id, @RequestBody UploadedFile uploadedFile) {
        IDataResult<UploadedFile> result = uploadedFileService.update(id, uploadedFile);
        if (result.isSuccess()) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFile(@PathVariable String id) {
        IResult result = uploadedFileService.deleteFile(id);
        if (result.isSuccess()) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        }
    }

    @DeleteMapping("/{id}/soft")
    public ResponseEntity<?> softDeleteFile(@PathVariable String id) {
        IResult result = uploadedFileService.softDeleteFile(id);
        if (result.isSuccess()) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        }
    }

    @GetMapping("/count")
    public ResponseEntity<?> getFileCount() {
        long count = uploadedFileService.getFileCount();
        return ResponseEntity.ok("Total files: " + count);
    }

    @GetMapping("/exists/{id}")
    public ResponseEntity<?> checkFileExists(@PathVariable String id) {
        boolean exists = uploadedFileService.fileExists(id);
        return ResponseEntity.ok("File exists: " + exists);
    }
    
    @GetMapping("/download/{id}")
    public ResponseEntity<?> downloadFile(@PathVariable String id) {
        IDataResult<Resource> resourceResult = uploadedFileService.loadFileAsResource(id);
        
        if (resourceResult.isSuccess()) {
            String filename = uploadedFileService.getOriginalFileName(id);
            Resource resource = resourceResult.getData();
            
            return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .body(resource);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resourceResult);
        }
    }
    
    @GetMapping("/api-path/{id}")
    public ResponseEntity<?> getApiFilePath(@PathVariable String id) {
        String apiFilePath = uploadedFileService.getApiFilePath(id);
        if (apiFilePath != null) {
            return ResponseEntity.ok(apiFilePath);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("API file path not found for id: " + id);
        }
    }
} 