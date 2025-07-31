package tr.shadowise_api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GenerateFlashcardsResponseDto {
    private List<FlashcardPair> flashcards;
    private int num_pairs;
    private String file_path;
    
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FlashcardPair {
        private String question;
        private String answer;
    }
}