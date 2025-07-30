package tr.shadowise_api.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GenerateFlashcardsRequestDto {
    private String content;
    private String projectId;
    private int cardCount = 10;
} 