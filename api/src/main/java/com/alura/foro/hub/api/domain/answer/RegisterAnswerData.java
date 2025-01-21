package com.alura.foro.hub.api.domain.answer;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RegisterAnswerData(
        @NotBlank String message,
        @NotNull Long usuarioId,
        @NotNull Long topicoId

) {
}
