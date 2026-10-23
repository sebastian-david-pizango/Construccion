package com.collectiverse.collectiverse.cu.shared.exception;

/** Base de las excepciones por regla de negocio, ej. registro duplicado (se responde 409). */
public class ConflictException extends RuntimeException {

    public ConflictException(String message) {
        super(message);
    }
}
