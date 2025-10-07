package tr.shadowise_api.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Note {
    private String title;
    private String content; 
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Project project;
}
