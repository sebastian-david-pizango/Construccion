package com.collectiverse.collectiverse.cu.favorite.request;

import jakarta.validation.constraints.NotNull;

/** Datos que envía el cliente en este caso de uso. */
public record RequestFavorite(
        @NotNull(message = "La publicación es obligatoria")
        Integer idPublication) {
}
