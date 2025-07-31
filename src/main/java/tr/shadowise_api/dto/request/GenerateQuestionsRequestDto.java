package tr.shadowise_api.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GenerateQuestionsRequestDto {
    private String cleaned_file_path;
    private int num_questions;
} 