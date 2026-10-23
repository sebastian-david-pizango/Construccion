package com.collectiverse.collectiverse.cu.publicationExchange.response;

/** Datos que se devuelven al cliente (nunca se expone la entidad completa). */
public record ResponsePublicationExchange(Integer id, Integer idPublication, String requestedName,
        String description, Integer quantity, Boolean status) {
}
