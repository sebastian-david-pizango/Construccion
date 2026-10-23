package com.collectiverse.collectiverse.cu.publication.response;

import java.time.LocalDateTime;
import java.util.List;

/** Datos que se devuelven al cliente (nunca se expone la entidad completa). */
public record ResponsePublication(Integer id, String username, Integer idProduct, String productName,
        String categoryName, String title, String description, String type, String state,
        LocalDateTime date, List<ResponsePublicationExchange> exchanges, Boolean status) {

    public record ResponsePublicationExchange(Integer id, String requestedName, String description, Integer quantity) {
    }
}
