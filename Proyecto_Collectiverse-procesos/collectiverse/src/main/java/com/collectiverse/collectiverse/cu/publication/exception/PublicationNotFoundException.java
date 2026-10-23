package com.collectiverse.collectiverse.cu.publication.exception;

import com.collectiverse.collectiverse.cu.shared.exception.ResourceNotFoundException;

/** Se lanza cuando no existe la publicación con el id indicado. */
public class PublicationNotFoundException extends ResourceNotFoundException {

    public PublicationNotFoundException(Integer id) {
        super("La publicación no encontrado con id: " + id);
    }
}
