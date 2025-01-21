package com.alura.foro.hub.api.domain.user.validation;

import com.alura.foro.hub.api.domain.user.UpdateUserData;

public interface ValidateUpdateUser {
    void validate(UpdateUserData data);
}
