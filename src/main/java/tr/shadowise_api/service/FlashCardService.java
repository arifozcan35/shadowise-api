package tr.shadowise_api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tr.shadowise_api.core.response.*;
import tr.shadowise_api.entity.FlashCard;
import tr.shadowise_api.repository.FlashCardRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FlashCardService {
    private final FlashCardRepository flashCardRepository;

    public IDataResult<List<FlashCard>> getAll() {
        try {
            List<FlashCard> flashCards = flashCardRepository.findAll();
            return new SuccessDataResult<>(flashCards, "FlashCards retrieved successfully");
        } catch (Exception e) {
            return new ErrorDataResult<>(null, "Failed to retrieve flashCards: " + e.getMessage());
        }
    }

    public IDataResult<FlashCard> getById(String id) {
        try {
            Optional<FlashCard> flashCard = flashCardRepository.findById(id);
            if (flashCard.isPresent()) {
                return new SuccessDataResult<>(flashCard.get(), "FlashCard retrieved successfully");
            } else {
                return new ErrorDataResult<>(null, "FlashCard not found with id: " + id);
            }
        } catch (Exception e) {
            return new ErrorDataResult<>(null, "Failed to retrieve flashCard: " + e.getMessage());
        }
    }

    public IDataResult<FlashCard> create(FlashCard flashCard) {
        try {
            flashCard.setCreatedAt(LocalDateTime.now());
            flashCard.setUpdatedAt(LocalDateTime.now());
            FlashCard savedFlashCard = flashCardRepository.save(flashCard);
            return new SuccessDataResult<>(savedFlashCard, "FlashCard created successfully");
        } catch (Exception e) {
            return new ErrorDataResult<>(null, "Failed to create flashCard: " + e.getMessage());
        }
    }

    public IDataResult<FlashCard> update(String id, FlashCard newFlashCard) {
        try {
            Optional<FlashCard> existingFlashCard = flashCardRepository.findById(id);
            if (existingFlashCard.isPresent()) {
                FlashCard flashCard = existingFlashCard.get();
                flashCard.setTitle(newFlashCard.getTitle());
                flashCard.setDescription(newFlashCard.getDescription());
                flashCard.setCards(newFlashCard.getCards());
                flashCard.setUpdatedAt(LocalDateTime.now());
                
                FlashCard updatedFlashCard = flashCardRepository.save(flashCard);
                return new SuccessDataResult<>(updatedFlashCard, "FlashCard updated successfully");
            } else {
                return new ErrorDataResult<>(null, "FlashCard not found with id: " + id);
            }
        } catch (Exception e) {
            return new ErrorDataResult<>(null, "Failed to update flashCard: " + e.getMessage());
        }
    }

    /**
     * Soft delete flashCard
     */
    public IResult softDeleteFlashCard(String id) {
        try {
            Optional<FlashCard> existingFlashCard = flashCardRepository.findById(id);
            if (existingFlashCard.isPresent()) {
                FlashCard flashCard = existingFlashCard.get();
                flashCard.setDeletedAt(LocalDateTime.now());
                flashCard.setUpdatedAt(LocalDateTime.now());
                
                flashCardRepository.save(flashCard);
                return new SuccessResult("FlashCard soft deleted successfully");
            } else {
                return new ErrorResult("FlashCard not found with id: " + id);
            }
        } catch (Exception e) {
            return new ErrorResult("Failed to soft delete flashCard: " + e.getMessage());
        }
    }

    /**
     * Hard delete flashCard
     */
    public IResult deleteFlashCard(String id) {
        try {
            if (flashCardRepository.existsById(id)) {
                flashCardRepository.deleteById(id);
                return new SuccessResult("FlashCard deleted successfully");
            } else {
                return new ErrorResult("FlashCard not found with id: " + id);
            }
        } catch (Exception e) {
            return new ErrorResult("Failed to delete flashCard: " + e.getMessage());
        }
    }
    
    /**
     * Check if flashCard exists
     */
    public boolean flashCardExists(String id) {
        return flashCardRepository.existsById(id);
    }

    /**
     * Get flashCard count
     */
    public long getFlashCardCount() {
        return flashCardRepository.count();
    }
} 