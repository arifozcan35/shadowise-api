package tr.shadowise_api.entity;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BaseEntity {
    @Id
    @NotNull
    private String Id;
    @NotNull
    private LocalDateTime CreatedAt;
    @NotNull
    private LocalDateTime UpdatedAt;
}
