package com.collectiverse.collectiverse.cu.rating.exception;

import com.collectiverse.collectiverse.cu.shared.exception.ResourceNotFoundException;

/** Se lanza cuando no existe la calificación con el id indicado. */
public class RatingNotFoundException extends ResourceNotFoundException {

    public RatingNotFoundException(Integer id) {
        super("La calificación no encontrado con id: " + id);
    }
}
