package com.collectiverse.collectiverse.cu.publicationExchange.exception;

import com.collectiverse.collectiverse.cu.shared.exception.ResourceNotFoundException;

/** Se lanza cuando no existe el intercambio con el id indicado. */
public class PublicationExchangeNotFoundException extends ResourceNotFoundException {

    public PublicationExchangeNotFoundException(Integer id) {
        super("El intercambio no encontrado con id: " + id);
    }
}
