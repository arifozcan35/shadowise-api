package tr.shadowise_api.entity;

import jakarta.validation.constraints.NotNull;

public class Choice {
    @NotNull
    private int id;
    @NotNull
    private String text;
}
