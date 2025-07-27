package tr.shadowise_api.entity;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class UploadedFile extends SoftDeleteEntity{
    @NotNull
    private String fileName;
    @NotNull
    private String fileType;
    @NotNull
    private String filePath; // local or S3 url
    @NotNull
    private Project project;
}
