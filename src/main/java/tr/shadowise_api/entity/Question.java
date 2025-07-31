package tr.shadowise_api.entity;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Question {
    @NotNull
    private String questionText;
    @NotNull
    private int correctAnswerIndex;
    @NotNull
    private List<String> choices;
}
