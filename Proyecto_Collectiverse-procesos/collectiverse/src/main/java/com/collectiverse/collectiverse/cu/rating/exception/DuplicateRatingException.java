package com.collectiverse.collectiverse.cu.rating.exception;

import com.collectiverse.collectiverse.cu.shared.exception.ConflictException;

/** Se lanza cuando el usuario ya calificó esa publicación. */
public class DuplicateRatingException extends ConflictException {

    public DuplicateRatingException(String message) {
        super(message);
    }
}
