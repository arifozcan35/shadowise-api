package tr.shadowise_api.entity;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.Date;

public class SoftDeleteEntity extends BaseEntity{
    @NotNull
    private LocalDateTime DeletedAt;
}
