package com.collectiverse.collectiverse.cu.collectionEntity.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/** Datos que envía el cliente en este caso de uso. */
public record RequestCollection(
        @NotNull(message = "La categoría es obligatoria")
        Integer idCategory,
        @NotBlank(message = "El nombre de la colección es obligatorio")
        @Size(max = 100, message = "El nombre no puede pasar de 100 caracteres")
        String name,
        @Size(max = 255, message = "La descripción no puede pasar de 255 caracteres")
        String description) {
}
