package com.alura.foro.hub.api.domain.answer;

public record UpdateAnswerData(
        String message,
        Boolean solution
) {
}
