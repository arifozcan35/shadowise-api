package tr.shadowise_api.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "summaries")
public class Summary extends SoftDeleteEntity {
    private String content;
    private Integer maxWords;
    private Double temperature;
    private String fileId;
    private String projectId;
}