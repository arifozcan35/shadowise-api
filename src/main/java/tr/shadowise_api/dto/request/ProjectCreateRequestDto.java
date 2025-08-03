package tr.shadowise_api.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import tr.shadowise_api.entity.UploadedFile;

import java.util.List;
import java.util.ArrayList;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProjectCreateRequestDto {
    @NotNull
    private String title;
    @NotNull
    private String description;
    @NotNull
    private List<UploadedFile> uploadedFiles = new ArrayList<>();
    
    // Constructor for title and description only
    public ProjectCreateRequestDto(String title, String description) {
        this.title = title;
        this.description = description;
    }
}
