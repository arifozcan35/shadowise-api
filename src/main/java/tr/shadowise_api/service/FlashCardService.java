package tr.shadowise_api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tr.shadowise_api.core.response.*;
import tr.shadowise_api.entity.FlashCard;
import tr.shadowise_api.repository.FlashCardRepository;
import tr.shadowise_api.dto.response.GenerateFlashcardsResponseDto;
import tr.shadowise_api.dto.response.GenerateFlashcardsResponseDto.FlashcardPair;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FlashCardService {
    private final FlashCardRepository flashCardRepository;
    private final AIService aiService;
    private final UploadedFileService uploadedFileService;

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
                flashCard.setFlashcards(newFlashCard.getFlashcards());
                flashCard.setFileId(newFlashCard.getFileId());
                flashCard.setProjectId(newFlashCard.getProjectId());
                flashCard.setNumPairs(newFlashCard.getNumPairs());
                flashCard.setFilePath(newFlashCard.getFilePath());
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
     * Generate flashcards from file using AI service
     */
    public IDataResult<FlashCard> generateFlashcardsFromFile(String fileId, Integer numPairs) {
        try {
            // Get API file path from file ID
            String apiFilePath = uploadedFileService.getApiFilePath(fileId);
            if (apiFilePath == null) {
                return new ErrorDataResult<>(null, "File not found with id: " + fileId);
            }
            
            // Get project ID from file
            String projectId = uploadedFileService.getProjectId(fileId);
            
            // Call AI service to generate flashcards
            IDataResult<GenerateFlashcardsResponseDto> aiResult = aiService.generateFlashcards(apiFilePath, numPairs);
            
            if (!aiResult.isSuccess()) {
                return new ErrorDataResult<>(null, "Failed to generate flashcards: " + aiResult.getMessage());
            }
            
            GenerateFlashcardsResponseDto aiResponse = aiResult.getData();
            
            // Create FlashCard entity
            FlashCard flashCard = new FlashCard();
            flashCard.setTitle("Flashcards for file: " + fileId);
            flashCard.setDescription("AI generated flashcards");
            flashCard.setFileId(fileId);
            flashCard.setProjectId(projectId);
            flashCard.setNumPairs(numPairs);
            flashCard.setFilePath(aiResponse.getFile_path());
            
            // Convert AI response to FlashCard entity format
            List<FlashCard.FlashcardPair> flashcardPairs = new java.util.ArrayList<>();
            for (FlashcardPair pair : aiResponse.getFlashcards()) {
                FlashCard.FlashcardPair entityPair = new FlashCard.FlashcardPair();
                entityPair.setQuestion(pair.getQuestion());
                entityPair.setAnswer(pair.getAnswer());
                flashcardPairs.add(entityPair);
            }
            flashCard.setFlashcards(flashcardPairs);
            
            // Save to database
            flashCard.setCreatedAt(LocalDateTime.now());
            flashCard.setUpdatedAt(LocalDateTime.now());
            FlashCard savedFlashCard = flashCardRepository.save(flashCard);
            
            return new SuccessDataResult<>(savedFlashCard, "Flashcards generated and saved successfully");
        } catch (Exception e) {
            return new ErrorDataResult<>(null, "Failed to generate flashcards: " + e.getMessage());
        }
    }

    /**
     * Get flashcards by file ID
     */
    public IDataResult<List<FlashCard>> getFlashcardsByFileId(String fileId) {
        try {
            List<FlashCard> flashCards = flashCardRepository.findByFileId(fileId);
            return new SuccessDataResult<>(flashCards, "FlashCards retrieved successfully for file: " + fileId);
        } catch (Exception e) {
            return new ErrorDataResult<>(null, "Failed to retrieve flashCards for file: " + e.getMessage());
        }
    }

    /**
     * Get flashcards by project ID
     */
    public IDataResult<List<FlashCard>> getFlashcardsByProjectId(String projectId) {
        try {
            List<FlashCard> flashCards = flashCardRepository.findByProjectId(projectId);
            return new SuccessDataResult<>(flashCards, "FlashCards retrieved successfully for project: " + projectId);
        } catch (Exception e) {
            return new ErrorDataResult<>(null, "Failed to retrieve flashCards for project: " + e.getMessage());
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