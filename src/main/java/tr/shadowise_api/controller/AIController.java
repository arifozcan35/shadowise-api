package tr.shadowise_api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tr.shadowise_api.core.response.IDataResult;
import tr.shadowise_api.dto.request.GenerateFlashcardsRequestDto;
import tr.shadowise_api.dto.request.GenerateProjectQuestionsRequestDto;
import tr.shadowise_api.dto.request.GenerateQuestionsRequestDto;
import tr.shadowise_api.dto.request.GenerateSummaryRequestDto;
import tr.shadowise_api.service.UploadedFileService;
import tr.shadowise_api.dto.response.GenerateFlashcardsResponseDto;
import tr.shadowise_api.dto.response.GenerateQuestionsResponseDto;
import tr.shadowise_api.service.AIService;

import java.util.Map;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
@CrossOrigin
@Tag(name = "AI API", description = "AI-powered content generation endpoints")
public class AIController {

    private final AIService aiService;
    private final UploadedFileService uploadedFileService;

    @Operation(summary = "Health Check", description = "Check if the AI service is running and healthy")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "AI service is healthy",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = IDataResult.class)))
    })
    @GetMapping("/health")
    public ResponseEntity<IDataResult<Map<String, Object>>> healthCheck() {
        return ResponseEntity.ok(aiService.healthCheck());
    }

    @Operation(summary = "Upload PDF", description = "Upload a PDF file for processing by the AI service")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "PDF uploaded successfully",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = IDataResult.class))),
        @ApiResponse(responseCode = "400", description = "Invalid file format or size"),
        @ApiResponse(responseCode = "500", description = "Upload failed")
    })
    @PostMapping(value = "/upload-pdf", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<IDataResult<Map<String, Object>>> uploadPdf(
            @Parameter(description = "PDF file to upload", required = true)
            @RequestPart("file") MultipartFile file) {
        return ResponseEntity.ok(aiService.uploadPdf(file));
    }

    @Operation(summary = "Generate Summary", description = "Generate a summary from cleaned file")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Summary generated successfully",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "500", description = "Summary generation failed")
    })
    @PostMapping("/generate-summary")
    public ResponseEntity<IDataResult<String>> generateSummary(@RequestBody GenerateSummaryRequestDto request) {
        return ResponseEntity.ok(aiService.generateSummary(
                request.getCleaned_file_path(), 
                request.getMax_words(), 
                request.getTemperature()));
    }

    @Operation(summary = "Generate Questions", description = "Generate quiz questions from content")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Questions generated successfully",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = GenerateQuestionsResponseDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "500", description = "Question generation failed")
    })
    @PostMapping("/generate-questions")
    public ResponseEntity<IDataResult<GenerateQuestionsResponseDto>> generateQuestions(@RequestBody GenerateQuestionsRequestDto request) {
        return ResponseEntity.ok(aiService.generateQuestions(
                request.getCleaned_file_path(),
                request.getNum_questions()));
    }

    @Operation(summary = "Generate Flashcards", description = "Generate flashcards from content")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Flashcards generated successfully",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = GenerateFlashcardsResponseDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "500", description = "Flashcard generation failed")
    })
    @PostMapping("/generate-flashcards")
    public ResponseEntity<IDataResult<GenerateFlashcardsResponseDto>> generateFlashcards(@RequestBody GenerateFlashcardsRequestDto request) {
        return ResponseEntity.ok(aiService.generateFlashcards(
                request.getCleaned_file_path(), 
                request.getNum_pairs()));
    }
    
    @Operation(summary = "Generate Project Questions", description = "Generate quiz questions from project file")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Questions generated successfully",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = GenerateQuestionsResponseDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "500", description = "Question generation failed")
    })
    @PostMapping("/generate-project-questions")
    public ResponseEntity<IDataResult<GenerateQuestionsResponseDto>> generateProjectQuestions(@RequestBody GenerateProjectQuestionsRequestDto request) {
        // Get API file path from file ID
        String apiFilePath = uploadedFileService.getApiFilePath(request.getFileId());
        if (apiFilePath == null) {
            return ResponseEntity.badRequest().body(null);
        }
        
        return ResponseEntity.ok(aiService.generateQuestions(
                apiFilePath,
                request.getNum_questions()));
    }
} 