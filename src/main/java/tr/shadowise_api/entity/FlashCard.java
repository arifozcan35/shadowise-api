package tr.shadowise_api.entity;

import java.util.Dictionary;

import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "flashcards")
public class FlashCard extends SoftDeleteEntity {
    @NotNull
    private String text;
    @NotNull
    private String definition;
    @NotNull
    private Dictionary<String,String> cards;
}
