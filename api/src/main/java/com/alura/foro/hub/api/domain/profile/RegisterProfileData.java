package com.alura.foro.hub.api.domain.profile;

import jakarta.validation.constraints.NotNull;

public record RegisterProfileData(
        @NotNull String name
) {
}
