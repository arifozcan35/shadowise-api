package tr.shadowise_api.entity;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
public class SoftDeleteEntity extends BaseEntity{
    @NotNull
    private LocalDateTime deletedAt;
}
