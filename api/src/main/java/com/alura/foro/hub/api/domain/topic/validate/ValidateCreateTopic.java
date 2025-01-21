package com.alura.foro.hub.api.domain.topic.validate;

import com.alura.foro.hub.api.domain.topic.RegisterTopicData;

public interface ValidateCreateTopic {
    void validate(RegisterTopicData data);
}
