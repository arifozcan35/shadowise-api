package tr.shadowise_api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tr.shadowise_api.core.response.IDataResult;
import tr.shadowise_api.core.response.IResult;
import tr.shadowise_api.core.response.SuccessDataResult;
import tr.shadowise_api.dto.request.ProjectCreateRequestDto;
import tr.shadowise_api.dto.response.DashboardStatsResponseDto;
import tr.shadowise_api.dto.response.ProjectResponseDto;
import tr.shadowise_api.entity.Project;
import tr.shadowise_api.entity.UploadedFile;
import tr.shadowise_api.entity.User;
import tr.shadowise_api.service.AIService;
import tr.shadowise_api.service.ProjectService;
import tr.shadowise_api.service.UploadedFileService;
import tr.shadowise_api.service.UserService;
import tr.shadowise_api.utils.FileUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/projects")
public class ProjectController {
    private final ProjectService projectService;
    private final UserService userService;
    private final AIService aiService;
    private final UploadedFileService uploadedFileService;
    private final FileUtils fileUtils;

    @GetMapping
    public ResponseEntity<?> getAllProjects() {
        IDataResult<List<Project>> result = projectService.getAllProjects();
        if (result.isSuccess()) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProjectById(@PathVariable String id) {
        IDataResult<Project> result = projectService.getProjectById(id);
        if (result.isSuccess()) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        }
    }

    @GetMapping("/my-projects")
    public ResponseEntity<?> getMyProjects() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            User currentUser = userService.findByUsername(username);
            
            if (currentUser == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("User not found");
            }
            
            IDataResult<List<Project>> result = projectService.getProjectsByOwner(currentUser);
            if (result.isSuccess()) {
                return ResponseEntity.ok(result);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error retrieving user projects: " + e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> createProject(@RequestBody ProjectCreateRequestDto projectCreateRequestDto) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            User currentUser = userService.findByUsername(username);
            
            if (currentUser == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("User not found");
            }
            
            IDataResult<Project> result = projectService.createProject(projectCreateRequestDto, currentUser);
            if (result.isSuccess()) {
                return ResponseEntity.status(HttpStatus.CREATED).body(result);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error creating project: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProject(@PathVariable String id, 
                                         @RequestBody ProjectCreateRequestDto projectCreateRequestDto) {
        IDataResult<Project> result = projectService.updateProject(id, projectCreateRequestDto);
        if (result.isSuccess()) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        }
    }

    @PutMapping("/{id}/tags")
    public ResponseEntity<?> updateProjectTags(@PathVariable String id, @RequestBody String[] tags) {
        IDataResult<Project> result = projectService.updateProjectTags(id, tags);
        if (result.isSuccess()) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        }
    }

    @PutMapping("/{id}/documents")
    public ResponseEntity<?> updateProjectDocuments(@PathVariable String id, @RequestBody String[] documents) {
        IDataResult<Project> result = projectService.updateProjectDocuments(id, documents);
        if (result.isSuccess()) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        }
    }

    @PutMapping("/{id}/insights")
    public ResponseEntity<?> updateProjectInsights(@PathVariable String id, @RequestBody String[] insights) {
        IDataResult<Project> result = projectService.updateProjectInsights(id, insights);
        if (result.isSuccess()) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProject(@PathVariable String id) {
        IResult result = projectService.deleteProject(id);
        if (result.isSuccess()) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        }
    }

    @PostMapping(value = "/create-with-files", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createProjectWithFiles(
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestPart("files") MultipartFile[] files) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            User currentUser = userService.findByUsername(username);
            
            if (currentUser == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("User not found");
            }
            
            IDataResult<Project> projectResult = projectService.createProject(title, description, currentUser);
            
            if (!projectResult.isSuccess()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(projectResult);
            }
            
            Project project = projectResult.getData();
            List<UploadedFile> uploadedFiles = new ArrayList<>();

            for (MultipartFile file : files) {
                try {
                    String originalFilePath = fileUtils.storeFile(file);
                    
                    IDataResult<Map<String, Object>> uploadResult = aiService.uploadPdf(file);
                    
                    UploadedFile uploadedFile = new UploadedFile();
                    uploadedFile.setFileName(file.getOriginalFilename());
                    uploadedFile.setFileType(file.getContentType());
                    uploadedFile.setProjectId(project.getId());
                    uploadedFile.setCreatedAt(LocalDateTime.now());
                    uploadedFile.setUpdatedAt(LocalDateTime.now());
                    
                    uploadedFile.setFilePath(originalFilePath);
                    
                    if (uploadResult.isSuccess() && uploadResult.getData() != null) {
                        // Get AI processing file path from the AI service response
                        Map<String, Object> data = uploadResult.getData();
                        String apiFilePath = null;
                        
                        if (data.containsKey("data") && data.get("data") instanceof Map) {
                            @SuppressWarnings("unchecked")
                            Map<String, Object> innerData = (Map<String, Object>) data.get("data");
                            
                            if (innerData.containsKey("cleaned_file_path")) {
                                apiFilePath = (String) innerData.get("cleaned_file_path");
                            } else if (innerData.containsKey("file_path")) {
                                apiFilePath = (String) innerData.get("file_path");
                            }
                        } else {
                            if (data.containsKey("cleaned_file_path")) {
                                apiFilePath = (String) data.get("cleaned_file_path");
                            } else if (data.containsKey("file_path")) {
                                apiFilePath = (String) data.get("file_path");
                            }
                        }
                        
                        if (apiFilePath != null) {
                            uploadedFile.setApiFilePath(apiFilePath);
                            System.out.println("API processing successful for file: " + file.getOriginalFilename() + 
                                             ", API path: " + apiFilePath);
                        }
                    }
                    
                    IDataResult<UploadedFile> savedFileResult = uploadedFileService.create(uploadedFile);
                    if (savedFileResult.isSuccess()) {
                        uploadedFiles.add(savedFileResult.getData());
                    } else {
                        uploadedFiles.add(uploadedFile);
                    }
                } catch (Exception e) {
                    System.err.println("Error processing file: " + e.getMessage());
                }
            }
            
            project.setUploadedFiles(uploadedFiles);
            IDataResult<Project> updateResult = projectService.updateProjectFiles(project.getId(), uploadedFiles);
            
            if (updateResult.isSuccess()) {
                // Convert to ProjectResponseDto to limit what fields we show
                ProjectResponseDto responseDto = new ProjectResponseDto(updateResult.getData());
                return ResponseEntity.status(HttpStatus.CREATED).body(
                    new SuccessDataResult<>(responseDto, "Project files updated successfully")
                );
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(updateResult);
            }
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error creating project with files: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}/soft")
    public ResponseEntity<?> softDeleteProject(@PathVariable String id) {
        IResult result = projectService.softDeleteProject(id);
        if (result.isSuccess()) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        }
    }

    @GetMapping("/count")
    public ResponseEntity<?> getProjectCount() {
        long count = projectService.getProjectCount();
        return ResponseEntity.ok("Total projects: " + count);
    }

    @GetMapping("/exists/{id}")
    public ResponseEntity<?> checkProjectExists(@PathVariable String id) {
        boolean exists = projectService.projectExists(id);
        return ResponseEntity.ok("Project exists: " + exists);
    }
    
    @GetMapping("/dashboard-stats")
    public ResponseEntity<?> getDashboardStats() {
        IDataResult<DashboardStatsResponseDto> result = projectService.getDashboardStats();
        if (result.isSuccess()) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }
}
