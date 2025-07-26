package tr.shadowise_api.entity;

import com.mongodb.lang.Nullable;
import jakarta.validation.constraints.NotNull;
import tr.shadowise_api.entity.enums.QuizLevel;

import java.util.Date;

public class Quiz extends SoftDeleteEntity{
    @NotNull
    private String name;
    @NotNull
    private String description;
    @NotNull
    private QuizLevel level;
    @Nullable
    private int timeLimit;
    @NotNull
    private double bestScore;
    @NotNull
    private Question[] questions;
    @NotNull
    private Date lastAttempt;
}
