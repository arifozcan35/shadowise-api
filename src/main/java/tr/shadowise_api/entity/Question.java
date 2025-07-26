package tr.shadowise_api.entity;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public class Question {
    @NotNull
    private String text;
    @NotNull
    private int answer;
    @NotNull
    private Choice[] choices;
}
