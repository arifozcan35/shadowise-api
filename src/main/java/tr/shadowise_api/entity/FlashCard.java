package tr.shadowise_api.entity;

import java.util.Dictionary;

import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Dictionary;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "flashcards")
public class FlashCard extends SoftDeleteEntity {
    @NotNull
    private String title;
    private String description;
    private Dictionary<String,String> cards;
}
