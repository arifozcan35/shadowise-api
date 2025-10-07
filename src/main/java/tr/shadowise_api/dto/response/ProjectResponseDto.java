package tr.shadowise_api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tr.shadowise_api.entity.Note;
import tr.shadowise_api.entity.Project;
import tr.shadowise_api.entity.UploadedFile;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectResponseDto {
    private String id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String title;
    private String description;
    private String[] tags;
    private String[] documents;
    private String[] insights;
    private String ownerId;  
    private List<UploadedFile> uploadedFiles;
    private List<Note> notes;
    
    // Constructor to convert Project entity to ProjectResponseDto
    public ProjectResponseDto(Project project) {
        this.id = project.getId();
        this.createdAt = project.getCreatedAt();
        this.updatedAt = project.getUpdatedAt();
        this.title = project.getTitle();
        this.description = project.getDescription();
        this.tags = project.getTags();
        this.documents = project.getDocuments();
        this.insights = project.getInsights();
        this.ownerId = (project.getOwner() != null) ? project.getOwner().getId() : null;
        this.uploadedFiles = project.getUploadedFiles();
        this.notes = project.getNotes();
    }
}