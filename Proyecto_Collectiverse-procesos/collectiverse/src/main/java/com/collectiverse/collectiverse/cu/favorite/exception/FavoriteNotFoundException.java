package com.collectiverse.collectiverse.cu.favorite.exception;

import com.collectiverse.collectiverse.cu.shared.exception.ResourceNotFoundException;

/** Se lanza cuando no existe el favorito con el id indicado. */
public class FavoriteNotFoundException extends ResourceNotFoundException {

    public FavoriteNotFoundException(Integer id) {
        super("El favorito no encontrado con id: " + id);
    }
}
