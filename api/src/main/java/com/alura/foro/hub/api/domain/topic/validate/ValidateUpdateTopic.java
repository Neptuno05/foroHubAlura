package com.alura.foro.hub.api.domain.topic.validate;

import com.alura.foro.hub.api.domain.topic.UpdateTopicData;

public interface ValidateUpdateTopic {
    void validate(UpdateTopicData data);
}
