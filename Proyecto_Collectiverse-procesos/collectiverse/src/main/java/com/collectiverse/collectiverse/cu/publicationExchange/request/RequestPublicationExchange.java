package com.collectiverse.collectiverse.cu.publicationExchange.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/** Datos que envía el cliente en este caso de uso. */
public record RequestPublicationExchange(
        @NotNull(message = "La publicación es obligatoria")
        Integer idPublication,
        @NotBlank(message = "El nombre solicitado es obligatorio")
        @Size(max = 100, message = "El nombre solicitado no puede pasar de 100 caracteres")
        String requestedName,
        @Size(max = 255, message = "La descripción no puede pasar de 255 caracteres")
        String description,
        @Min(value = 1, message = "La cantidad debe ser al menos 1")
        Integer quantity) {
}
