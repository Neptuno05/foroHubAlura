package com.alura.foro.hub.api.domain.answer.validate;

import com.alura.foro.hub.api.domain.answer.RegisterAnswerData;
import com.alura.foro.hub.api.domain.user.UserRepository;
import com.alura.foro.hub.api.infra.errores.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidateAnswerUser implements ValidateCreateAnswer {

    @Autowired
    private UserRepository repository;

    @Override
    public void validate(RegisterAnswerData data) {
        var usuarioExiste = repository.existsById(data.usuarioId());

        if(!usuarioExiste){
            throw new ValidationException("Este usuario no existe");
        }
    }

}
