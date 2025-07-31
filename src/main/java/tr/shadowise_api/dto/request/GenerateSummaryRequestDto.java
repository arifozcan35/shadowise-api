package tr.shadowise_api.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GenerateSummaryRequestDto {
    private String cleaned_file_path;
    private Integer max_words;
    private Double temperature;
} 