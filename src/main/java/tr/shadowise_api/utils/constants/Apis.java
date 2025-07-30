package tr.shadowise_api.utils.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Apis {

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Project {
        public static final String BASE_URL = "/projects";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class AI {
        public static final String BASE_URL = "/api/ai";
        public static final String HEALTH = "/health";
        public static final String UPLOAD_PDF = "/upload-pdf";
        public static final String GENERATE_SUMMARY = "/generate-summary";
        public static final String GENERATE_QUESTIONS = "/generate-questions";
        public static final String GENERATE_FLASHCARDS = "/generate-flashcards";
    }
}