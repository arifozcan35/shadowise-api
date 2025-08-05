package tr.shadowise_api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tr.shadowise_api.core.response.IDataResult;
import tr.shadowise_api.core.response.IResult;
import tr.shadowise_api.entity.FlashCard;
import tr.shadowise_api.service.FlashCardService;
import tr.shadowise_api.dto.request.GenerateFlashcardsRequestDto;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/flashcards")
public class FlashCardController {
    private final FlashCardService flashCardService;

    @GetMapping
    public ResponseEntity<?> getAllFlashCards() {
        IDataResult<List<FlashCard>> result = flashCardService.getAll();
        if (result.isSuccess()) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getFlashCardById(@PathVariable String id) {
        IDataResult<FlashCard> result = flashCardService.getById(id);
        if (result.isSuccess()) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        }
    }

    @PostMapping
    public ResponseEntity<?> createFlashCard(@RequestBody FlashCard flashCard) {
        IDataResult<FlashCard> result = flashCardService.create(flashCard);
        if (result.isSuccess()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
    }

    @PostMapping("/generate")
    public ResponseEntity<?> generateFlashcards(@RequestBody GenerateFlashcardsRequestDto request) {
        IDataResult<FlashCard> result = flashCardService.generateFlashcardsFromFile(
                request.getFileId(), 
                request.getNum_pairs());
        if (result.isSuccess()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
    }

    @GetMapping("/file/{fileId}")
    public ResponseEntity<?> getFlashcardsByFileId(@PathVariable String fileId) {
        IDataResult<List<FlashCard>> result = flashCardService.getFlashcardsByFileId(fileId);
        if (result.isSuccess()) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        }
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<?> getFlashcardsByProjectId(@PathVariable String projectId) {
        IDataResult<List<FlashCard>> result = flashCardService.getFlashcardsByProjectId(projectId);
        if (result.isSuccess()) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateFlashCard(@PathVariable String id, @RequestBody FlashCard flashCard) {
        IDataResult<FlashCard> result = flashCardService.update(id, flashCard);
        if (result.isSuccess()) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFlashCard(@PathVariable String id) {
        IResult result = flashCardService.deleteFlashCard(id);
        if (result.isSuccess()) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        }
    }

    @DeleteMapping("/{id}/soft")
    public ResponseEntity<?> softDeleteFlashCard(@PathVariable String id) {
        IResult result = flashCardService.softDeleteFlashCard(id);
        if (result.isSuccess()) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        }
    }

    @GetMapping("/count")
    public ResponseEntity<?> getFlashCardCount() {
        long count = flashCardService.getFlashCardCount();
        return ResponseEntity.ok("Total flashcards: " + count);
    }

    @GetMapping("/exists/{id}")
    public ResponseEntity<?> checkFlashCardExists(@PathVariable String id) {
        boolean exists = flashCardService.flashCardExists(id);
        return ResponseEntity.ok("FlashCard exists: " + exists);
    }
} 