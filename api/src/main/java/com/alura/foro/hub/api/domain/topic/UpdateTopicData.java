package com.alura.foro.hub.api.domain.topic;

public record UpdateTopicData(
        String title,
        String message,
        TopicStatus status,
        Long course_id
) {
}
