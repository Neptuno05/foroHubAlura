package com.alura.foro.hub.api.domain.user.validation;

import com.alura.foro.hub.api.domain.user.RegisterUserData;
import com.alura.foro.hub.api.domain.user.UserRepository;
import com.alura.foro.hub.api.infra.errores.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserDuplicate implements ValidateCreateUser{

    @Autowired
    private UserRepository repository;

    @Override
    public void validate(RegisterUserData data) {
        var usuarioDuplicado = repository.findByName(data.name());
        if(usuarioDuplicado != null){
            throw new ValidationException("Este usuario ya existe.");
        }

        var emailDuplicado = repository.findByEmail(data.email());
        if(emailDuplicado != null){
            throw new ValidationException("Este email ya existe.");
        }
    }
}
