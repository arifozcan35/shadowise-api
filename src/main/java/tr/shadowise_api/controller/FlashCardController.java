package tr.shadowise_api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tr.shadowise_api.entity.FlashCard;
import tr.shadowise_api.service.FlashCardService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/flashcards")
public class FlashCardController {
    private final FlashCardService flashCardService;

    @GetMapping
    public List<FlashCard> getAllFlashCards() {
        return flashCardService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<FlashCard> getFlashCardById(@PathVariable String id) {
        return flashCardService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FlashCard createFlashCard(@RequestBody FlashCard flashCard) {
        return flashCardService.create(flashCard);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FlashCard> updateFlashCard(@PathVariable String id, @RequestBody FlashCard flashCard) {
        try {
            FlashCard updatedFlashCard = flashCardService.update(id, flashCard);
            return ResponseEntity.ok(updatedFlashCard);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFlashCard(@PathVariable String id) {
        try {
            flashCardService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
} 