package com.alura.foro.hub.api.domain.answer.validate;

import com.alura.foro.hub.api.domain.answer.RegisterAnswerData;
import com.alura.foro.hub.api.domain.topic.TopicRepository;
import com.alura.foro.hub.api.domain.topic.TopicStatus;
import com.alura.foro.hub.api.infra.errores.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidateAnswerTopic implements ValidateCreateAnswer {

    @Autowired
    private TopicRepository repository;

    @Override
    public void validate(RegisterAnswerData data) {
        var topicoExiste = repository.existsById(data.topicoId());

        if (!topicoExiste){
            throw new ValidationException("This topic doesn't exist.");
        }

        var topicoAbierto = repository.findById(data.topicoId()).get().getStatus();

        if(topicoAbierto != TopicStatus.ACTIVE){
            throw new ValidationException("This topic is not active");
        }

    }

}
