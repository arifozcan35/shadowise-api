package tr.shadowise_api.entity;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Project extends SoftDeleteEntity {
    @NotNull
    private String name;
    @NotNull
    private String description;
    @NotNull
    private String[] tags;
    @NotNull
    private String[] documents;
    @NotNull
    private String[] insights;
}
