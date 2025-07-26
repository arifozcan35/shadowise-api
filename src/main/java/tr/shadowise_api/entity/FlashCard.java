package tr.shadowise_api.entity;

import jakarta.validation.constraints.NotNull;

public class FlashCard extends SoftDeleteEntity {
    @NotNull
    private String text;
    @NotNull
    private String definition;
}
