package com.collectiverse.collectiverse.cu.rating.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/** Datos que envía el cliente en este caso de uso. */
public record RequestRating(
        @NotNull(message = "La publicación es obligatoria")
        Integer idPublication,
        @Min(value = 1, message = "La calificación del usuario debe estar entre 1 y 5")
        @Max(value = 5, message = "La calificación del usuario debe estar entre 1 y 5")
        Integer userRating,
        @Min(value = 1, message = "La calificación de la publicación debe estar entre 1 y 5")
        @Max(value = 5, message = "La calificación de la publicación debe estar entre 1 y 5")
        Integer publicationRating,
        @Size(max = 255, message = "El comentario no puede pasar de 255 caracteres")
        String comment) {
}
