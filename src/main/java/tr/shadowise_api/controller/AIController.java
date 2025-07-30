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
import tr.shadowise_api.dto.request.GenerateQuestionsRequestDto;
import tr.shadowise_api.dto.request.GenerateSummaryRequestDto;
import tr.shadowise_api.entity.FlashCard;
import tr.shadowise_api.entity.Question;
import tr.shadowise_api.entity.Note;
import tr.shadowise_api.service.AIService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
@CrossOrigin
@Tag(name = "AI API", description = "AI-powered content generation endpoints")
public class AIController {

    private final AIService aiService;

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

    @Operation(summary = "Generate Summary", description = "Generate a summary from content or project")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Summary generated successfully",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = Note.class))),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "500", description = "Summary generation failed")
    })
    @PostMapping("/generate-summary")
    public ResponseEntity<IDataResult<Note>> generateSummary(@RequestBody GenerateSummaryRequestDto request) {
        if (request.getProjectId() != null && !request.getProjectId().isEmpty()) {
            return ResponseEntity.ok(aiService.generateSummaryFromProject(request.getProjectId()));
        } else {
            return ResponseEntity.ok(aiService.generateSummary(request.getContent()));
        }
    }

    @PostMapping("/generate-summary/project/{projectId}")
    public ResponseEntity<IDataResult<Note>> generateSummaryFromProject(@PathVariable String projectId) {
        return ResponseEntity.ok(aiService.generateSummaryFromProject(projectId));
    }

    @Operation(summary = "Generate Questions", description = "Generate quiz questions from content or project")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Questions generated successfully",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = Question.class))),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "500", description = "Question generation failed")
    })
    @PostMapping("/generate-questions")
    public ResponseEntity<IDataResult<List<Question>>> generateQuestions(@RequestBody GenerateQuestionsRequestDto request) {
        if (request.getProjectId() != null && !request.getProjectId().isEmpty()) {
            return ResponseEntity.ok(aiService.generateQuestionsFromProject(
                    request.getProjectId(), 
                    request.getQuestionCount(), 
                    request.getDifficulty()));
        } else {
            return ResponseEntity.ok(aiService.generateQuestions(
                    request.getContent(), 
                    request.getQuestionCount(), 
                    request.getDifficulty()));
        }
    }

    @PostMapping("/generate-questions/project/{projectId}")
    public ResponseEntity<IDataResult<List<Question>>> generateQuestionsFromProject(
            @PathVariable String projectId,
            @RequestParam(defaultValue = "5") int questionCount,
            @RequestParam(defaultValue = "medium") String difficulty) {
        return ResponseEntity.ok(aiService.generateQuestionsFromProject(projectId, questionCount, difficulty));
    }

    @Operation(summary = "Generate Flashcards", description = "Generate flashcards from content or project")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Flashcards generated successfully",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = FlashCard.class))),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "500", description = "Flashcard generation failed")
    })
    @PostMapping("/generate-flashcards")
    public ResponseEntity<IDataResult<FlashCard>> generateFlashcards(@RequestBody GenerateFlashcardsRequestDto request) {
        if (request.getProjectId() != null && !request.getProjectId().isEmpty()) {
            return ResponseEntity.ok(aiService.generateFlashcardsFromProject(
                    request.getProjectId(), 
                    request.getCardCount()));
        } else {
            return ResponseEntity.ok(aiService.generateFlashcards(
                    request.getContent(), 
                    request.getCardCount()));
        }
    }

    @PostMapping("/generate-flashcards/project/{projectId}")
    public ResponseEntity<IDataResult<FlashCard>> generateFlashcardsFromProject(
            @PathVariable String projectId,
            @RequestParam(defaultValue = "10") int cardCount) {
        return ResponseEntity.ok(aiService.generateFlashcardsFromProject(projectId, cardCount));
    }
} 