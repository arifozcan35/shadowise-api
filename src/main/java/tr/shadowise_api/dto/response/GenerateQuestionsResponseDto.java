package tr.shadowise_api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tr.shadowise_api.entity.Question;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GenerateQuestionsResponseDto {
    private List<Question> questions;
    private int num_questions;
    private String file_path;
}