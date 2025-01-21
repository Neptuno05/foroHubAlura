package com.alura.foro.hub.api.domain.user;

import com.alura.foro.hub.api.domain.profile.ProfileType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RegisterUserData(
        @NotBlank String name,
        @NotBlank @Email String email,
        @NotBlank String password,
        @NotNull ProfileType profileType
) {
}
