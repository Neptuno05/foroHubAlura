package com.alura.foro.hub.api.domain.user.validation;

import com.alura.foro.hub.api.domain.user.UpdateUserData;
import com.alura.foro.hub.api.domain.user.UserRepository;
import com.alura.foro.hub.api.infra.errores.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidateUpdatingUser implements ValidateUpdateUser{

    @Autowired
    private UserRepository repository;

    @Override
    public void validate(UpdateUserData data) {
        if(data.email() != null){
            var emailDuplicado = repository.findByEmail(data.email());
            if(emailDuplicado != null){
                throw new ValidationException("Este email ya esta en uso");
            }
        }
    }

}
