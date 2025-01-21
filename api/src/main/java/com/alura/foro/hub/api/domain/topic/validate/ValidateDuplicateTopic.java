package com.alura.foro.hub.api.domain.topic.validate;

import com.alura.foro.hub.api.domain.topic.RegisterTopicData;
import com.alura.foro.hub.api.domain.topic.TopicRepository;
import com.alura.foro.hub.api.infra.errores.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidateDuplicateTopic implements ValidateCreateTopic{
    @Autowired
    private TopicRepository topicRepository;


    @Override
    public void validate(RegisterTopicData data) {
        var topicoDuplicado = topicRepository.existsByTitleAndMessage(data.title(),
                data.message());
        if(topicoDuplicado){
            throw new ValidationException("This topic already exists. review (/topics/" +
                    topicRepository.findByTitleAndMessage(data.title(),
                            data.message()).getId() + ")");
        }
    }
}
