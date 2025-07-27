package tr.shadowise_api.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import tr.shadowise_api.entity.Note;
import tr.shadowise_api.entity.UploadedFile;
import tr.shadowise_api.entity.User;

import java.util.List;

@Getter
public class ProjectCreateRequestDto {
    @NotNull
    private String title;
    @NotNull
    private String description;
    @NotNull
    private List<UploadedFile> uploadedFiles;
}
