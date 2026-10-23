package com.collectiverse.collectiverse.cu.category.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/** Datos que envía el cliente en este caso de uso. */
public record RequestCategory(
        @NotBlank(message = "El nombre de la categoría es obligatorio")
        @Size(max = 50, message = "El nombre no puede pasar de 50 caracteres")
        String name,
        @Size(max = 255, message = "La descripción no puede pasar de 255 caracteres")
        String description) {
}
