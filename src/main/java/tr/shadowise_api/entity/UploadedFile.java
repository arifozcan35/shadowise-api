package tr.shadowise_api.entity;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "uploadedfiles")
public class UploadedFile extends SoftDeleteEntity{
    @NotNull
    private String fileName;
    @NotNull
    private String fileType;
    @NotNull
    private String filePath; // local or S3 url for download
    private String apiFilePath; // path from API for AI processing
    @NotNull
    private String projectId;
}
