package com.alura.foro.hub.api.domain.user.validation;

import com.alura.foro.hub.api.domain.user.RegisterUserData;

public interface ValidateCreateUser {
    void validate(RegisterUserData data);
}
