package com.collectiverse.collectiverse.cu.rating.response;

import java.time.LocalDateTime;

/** Datos que se devuelven al cliente (nunca se expone la entidad completa). */
public record ResponseRating(Integer id, String username, Integer idPublication, Integer userRating,
        Integer publicationRating, String comment, LocalDateTime date, Boolean status) {
}
