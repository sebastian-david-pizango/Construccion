package com.collectiverse.collectiverse.cu.shared.exception;

/** Solicitud inválida por regla de negocio, no por formato del body (se responde 400). */
public class BadRequestException extends RuntimeException {

    public BadRequestException(String message) {
        super(message);
    }
}
