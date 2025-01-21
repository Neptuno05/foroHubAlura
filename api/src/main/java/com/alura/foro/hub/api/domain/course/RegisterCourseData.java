package com.alura.foro.hub.api.domain.course;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RegisterCourseData(
        @NotBlank String name,
        @NotNull Category category
) {
}
