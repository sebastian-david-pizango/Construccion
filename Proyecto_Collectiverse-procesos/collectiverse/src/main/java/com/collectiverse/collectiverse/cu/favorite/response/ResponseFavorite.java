package com.collectiverse.collectiverse.cu.favorite.response;

import java.time.LocalDateTime;

/** Datos que se devuelven al cliente (nunca se expone la entidad completa). */
public record ResponseFavorite(Integer id, String username, Integer idPublication, LocalDateTime date, Boolean status) {
}
