package com.collectiverse.collectiverse.cu.shared.exception;

/** Base de todas las excepciones "no encontrado" (se responde 404). */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
