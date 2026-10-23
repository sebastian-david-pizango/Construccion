package com.collectiverse.collectiverse.cu.favorite.exception;

import com.collectiverse.collectiverse.cu.shared.exception.ConflictException;

/** Se lanza cuando el usuario ya marcó esa publicación como favorita. */
public class DuplicateFavoriteException extends ConflictException {

    public DuplicateFavoriteException(String message) {
        super(message);
    }
}
