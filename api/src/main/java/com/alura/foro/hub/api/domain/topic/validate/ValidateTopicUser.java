package com.alura.foro.hub.api.domain.topic.validate;

import com.alura.foro.hub.api.domain.topic.RegisterTopicData;
import com.alura.foro.hub.api.domain.user.UserRepository;
import com.alura.foro.hub.api.infra.errores.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidateTopicUser implements ValidateCreateTopic{

    @Autowired
    private UserRepository repository;

    @Override
    public void validate(RegisterTopicData data) {
        var existeUsuario = repository.existsById(data.userid());
        if (!existeUsuario) {
            throw new ValidationException("This user doesn't exist");
        }
    }

}
