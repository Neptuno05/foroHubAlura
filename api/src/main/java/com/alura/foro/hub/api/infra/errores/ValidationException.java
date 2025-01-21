package com.alura.foro.hub.api.infra.errores;

public class ValidationException extends RuntimeException {
    public ValidationException(String mensaje) {

        super(mensaje);
    }
}
