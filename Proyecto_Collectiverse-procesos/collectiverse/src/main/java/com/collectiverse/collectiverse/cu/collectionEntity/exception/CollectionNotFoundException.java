package com.collectiverse.collectiverse.cu.collectionEntity.exception;

import com.collectiverse.collectiverse.cu.shared.exception.ResourceNotFoundException;

/** Se lanza cuando no existe la colección con el id indicado. */
public class CollectionNotFoundException extends ResourceNotFoundException {

    public CollectionNotFoundException(Integer id) {
        super("La colección no encontrado con id: " + id);
    }
}
