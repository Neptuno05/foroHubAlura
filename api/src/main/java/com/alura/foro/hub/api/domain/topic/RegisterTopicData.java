package com.alura.foro.hub.api.domain.topic;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RegisterTopicData(
        @NotBlank String title,
        @NotBlank String message,
        @NotNull @JsonAlias({"id_user","user_id"}) Long userid,
        @NotNull @JsonAlias({"id_course","course_id"}) Long courseid
) {
}
