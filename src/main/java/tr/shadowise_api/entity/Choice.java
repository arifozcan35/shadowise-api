package tr.shadowise_api.entity;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Choice {
    @NotNull
    private int id;
    @NotNull
    private String text;
}
