package com.alura.foro.hub.api.domain.topic;

import com.alura.foro.hub.api.domain.course.Category;

import java.time.LocalDateTime;

public record DetailsTopicData(
        Long id,
        String title,
        String message,
        LocalDateTime createdDate,
        TopicStatus status,
        String user,
        String course,
        Category categoryCourse
) {
    public DetailsTopicData(Topic topic) {
        this(topic.getId(),
                topic.getTitle(),
                topic.getMessage(),
                topic.getCreatedDate(),
                topic.getStatus(),
                topic.getUser().getName(),
                topic.getCourse().getName(),
                topic.getCourse().getCategory()
        );
    }
}
