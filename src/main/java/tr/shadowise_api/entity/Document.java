package tr.shadowise_api.entity;

import jakarta.validation.constraints.NotNull;

public class Document extends SoftDeleteEntity{
    @NotNull
    private String name;
    @NotNull
    private String content;
}
