package tr.shadowise_api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tr.shadowise_api.entity.FlashCard;
import tr.shadowise_api.repository.FlashCardRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FlashCardService {
    private final FlashCardRepository flashCardRepository;

    public List<FlashCard> getAll() {
        return flashCardRepository.findAll();
    }

    public Optional<FlashCard> getById(String id) {
        return flashCardRepository.findById(id);
    }

    public FlashCard create(FlashCard flashCard) {
        return flashCardRepository.save(flashCard);
    }

    public FlashCard update(String id, FlashCard newFlashCard) {
        FlashCard flashCard = flashCardRepository.findById(id).orElseThrow();
        flashCard.setText(newFlashCard.getText());
        flashCard.setDefinition(newFlashCard.getDefinition());
        flashCard.setCards(newFlashCard.getCards());
        return flashCardRepository.save(flashCard);
    }

    public void delete(String id) {
        flashCardRepository.deleteById(id);
    }
} 