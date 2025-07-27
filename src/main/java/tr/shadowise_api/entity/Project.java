package tr.shadowise_api.entity;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "projects")
public class Project extends SoftDeleteEntity {
    @NotNull
    private String title;
    @NotNull
    private String description;
    @NotNull
    private String[] tags;
    @NotNull
    private String[] documents;
    @NotNull
    private String[] insights;
    private User owner;
    private List<UploadedFile> uploadedFiles;
    private List<Note> notes;
}
