package tr.shadowise_api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tr.shadowise_api.core.response.ErrorDataResult;
import tr.shadowise_api.core.response.IDataResult;
import tr.shadowise_api.entity.Project;
import tr.shadowise_api.entity.UploadedFile;
import tr.shadowise_api.service.AIService;
import tr.shadowise_api.service.ProjectService;
import tr.shadowise_api.service.UploadedFileService;

import java.util.List;

@RestController
@RequestMapping("/api/insights")
@RequiredArgsConstructor
@CrossOrigin
@Tag(name = "Insights API", description = "Document insights and summary generation endpoints")
public class InsightController {

    private final AIService aiService;
    private final UploadedFileService uploadedFileService;
    private final ProjectService projectService;

    @Operation(summary = "Generate Document Summary", description = "Generate summary for previously uploaded file")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Summary generated successfully",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "400", description = "Invalid file ID or request data"),
        @ApiResponse(responseCode = "404", description = "File not found"),
        @ApiResponse(responseCode = "500", description = "Processing failed")
    })
    @PostMapping(value = "/generate-summary")
    public ResponseEntity<IDataResult<String>> generateSummaryFromUpload(
            @Parameter(description = "File ID to summarize", required = true)
            @RequestParam("fileId") String fileId,
            @Parameter(description = "Maximum words in summary (optional)")
            @RequestParam(required = false, defaultValue = "500") Integer maxWords,
            @Parameter(description = "Temperature for AI generation (optional)")
            @RequestParam(required = false, defaultValue = "0.7") Double temperature) {
        
        // Get the API file path from the uploaded file
        String apiFilePath = uploadedFileService.getApiFilePath(fileId);
        
        if (apiFilePath == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorDataResult<>(null, "File not found or API file path is not available"));
        }
        
        // Generate and return the summary using the cleaned file path
        return ResponseEntity.ok(aiService.generateSummary(apiFilePath, maxWords, temperature));
    }
    
    @Operation(summary = "Generate Project Document Summary", description = "Generate summary for a file in a project")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Summary generated successfully",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "400", description = "Invalid project ID"),
        @ApiResponse(responseCode = "404", description = "Project not found or has no files"),
        @ApiResponse(responseCode = "500", description = "Processing failed")
    })
    @PostMapping(value = "/project-summary")
    public ResponseEntity<IDataResult<String>> generateProjectSummary(
            @Parameter(description = "Project ID", required = true)
            @RequestParam("projectId") String projectId,
            @Parameter(description = "File index in project (defaults to first file)", required = false)
            @RequestParam(required = false, defaultValue = "0") Integer fileIndex,
            @Parameter(description = "Maximum words in summary (optional)")
            @RequestParam(required = false, defaultValue = "500") Integer maxWords,
            @Parameter(description = "Temperature for AI generation (optional)")
            @RequestParam(required = false, defaultValue = "0.7") Double temperature) {
        
        // Get the project
        IDataResult<Project> projectResult = projectService.getProjectById(projectId);
        
        if (!projectResult.isSuccess() || projectResult.getData() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorDataResult<>(null, "Project not found with ID: " + projectId));
        }
        
        // Get the project files
        Project project = projectResult.getData();
        List<UploadedFile> files = project.getUploadedFiles();
        
        if (files == null || files.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorDataResult<>(null, "Project has no uploaded files"));
        }
        
        // Check if fileIndex is valid
        if (fileIndex < 0 || fileIndex >= files.size()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorDataResult<>(null, "Invalid file index: " + fileIndex));
        }
        
        // Get the file at the specified index
        UploadedFile file = files.get(fileIndex);
        String apiFilePath = file.getApiFilePath();
        
        if (apiFilePath == null || apiFilePath.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorDataResult<>(null, "API file path not available for the selected file"));
        }
        
        // Generate and return the summary using the cleaned file path
        return ResponseEntity.ok(aiService.generateSummary(apiFilePath, maxWords, temperature));
    }
}