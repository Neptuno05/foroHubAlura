package com.alura.foro.hub.api.domain.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record AutenticatioUserData(
        @NotBlank @Email String email,
        @NotBlank String password
        ) {
}
