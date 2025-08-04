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
import tr.shadowise_api.entity.Summary;
import tr.shadowise_api.service.AIService;
import tr.shadowise_api.service.SummaryService;
import tr.shadowise_api.service.UploadedFileService;

@RestController
@RequestMapping("/api/insights")
@RequiredArgsConstructor
@CrossOrigin
@Tag(name = "Insights API", description = "Document insights and summary generation endpoints")
public class InsightController {

    private final AIService aiService;
    private final UploadedFileService uploadedFileService;
    private final SummaryService summaryService;

    @Operation(summary = "Generate Document Summary", description = "Generate summary for previously uploaded file and save it to database")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Summary generated successfully",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = Summary.class))),
        @ApiResponse(responseCode = "400", description = "Invalid file ID or request data"),
        @ApiResponse(responseCode = "404", description = "File not found"),
        @ApiResponse(responseCode = "500", description = "Processing failed")
    })
    @PostMapping(value = "/generate-summary")
    public ResponseEntity<IDataResult<Summary>> generateSummaryFromUpload(
            @Parameter(description = "File ID to summarize", required = true)
            @RequestParam("fileId") String fileId,
            @Parameter(description = "Maximum words in summary (optional)")
            @RequestParam(required = false, defaultValue = "500") Integer maxWords,
            @Parameter(description = "Temperature for AI generation (optional)")
            @RequestParam(required = false, defaultValue = "0.7") Double temperature) {
        
        // Check if summary already exists with these parameters
        IDataResult<Summary> existingSummary = summaryService.getSummaryByFileIdAndParameters(fileId, maxWords, temperature);
        if (existingSummary.isSuccess()) {
            return ResponseEntity.ok(existingSummary);
        }
        
        // Get the API file path from the uploaded file
        String apiFilePath = uploadedFileService.getApiFilePath(fileId);
        
        if (apiFilePath == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorDataResult<>(null, "File not found or API file path is not available"));
        }
        
        // Generate summary
        IDataResult<String> summaryResult = aiService.generateSummary(apiFilePath, maxWords, temperature);
        if (!summaryResult.isSuccess()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorDataResult<>(null, "Failed to generate summary"));
        }
        
        // Save summary to database
        Summary summary = new Summary();
        summary.setContent(summaryResult.getData());
        summary.setFileId(fileId);
        summary.setMaxWords(maxWords);
        summary.setTemperature(temperature);
        
        // Get and set the project ID
        String projectId = uploadedFileService.getProjectId(fileId);
        summary.setProjectId(projectId);
        
        IDataResult<Summary> savedSummary = summaryService.saveSummary(summary);
        return ResponseEntity.ok(savedSummary);
    }
    
    @Operation(summary = "Get Summary By ID", description = "Retrieve a previously generated summary by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Summary retrieved successfully",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = Summary.class))),
        @ApiResponse(responseCode = "404", description = "Summary not found")
    })
    @GetMapping("/file-summary/{id}")
    public ResponseEntity<IDataResult<Summary>> getSummaryById(
            @Parameter(description = "Summary ID", required = true)
            @PathVariable String id) {
        
        IDataResult<Summary> summary = summaryService.getSummaryById(id);
        if (summary.isSuccess()) {
            return ResponseEntity.ok(summary);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(summary);
        }
    }
}