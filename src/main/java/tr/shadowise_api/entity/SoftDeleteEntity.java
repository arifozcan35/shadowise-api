package tr.shadowise_api.entity;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SoftDeleteEntity extends BaseEntity {
    private LocalDateTime deletedAt;
} 