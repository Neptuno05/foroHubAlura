package com.alura.foro.hub.api.domain.answer;

import java.time.LocalDateTime;

public record DetailsAnswerData(
        Long id,
        String message,
        LocalDateTime createdDate,
        Boolean solution,
        Long topicId,
        String topic,
        Long userId,
        String username
) {
    public DetailsAnswerData(Answer answer){
        this(
                answer.getId(),
                answer.getMessage(),
                answer.getCreatedDate(),
                answer.getSolution(),
                answer.getTopic().getId(),
                answer.getTopic().getTitle(),
                answer.getUser().getId(),
                answer.getUser().getName());
    }
}
