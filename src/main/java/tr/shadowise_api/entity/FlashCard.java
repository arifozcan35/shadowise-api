package tr.shadowise_api.entity;

import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "flashcards")
public class FlashCard extends SoftDeleteEntity {
    @NotNull
    private String title;
    private String description;
    private String fileId;
    private String projectId;
    private List<FlashcardPair> flashcards;
    private Integer numPairs;
    private String filePath;
    
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FlashcardPair {
        private String question;
        private String answer;
    }
}
