package tr.shadowise_api.entity;

import jakarta.validation.constraints.NotNull;

import java.util.Date;

public class SoftDeleteEntity extends BaseEntity{
    @NotNull
    private Date DeletedAt;
}
