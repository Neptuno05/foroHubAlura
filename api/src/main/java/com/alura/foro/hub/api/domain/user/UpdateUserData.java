package com.alura.foro.hub.api.domain.user;

public record UpdateUserData(
        String clave,
        String name,
        String email
) {
}
