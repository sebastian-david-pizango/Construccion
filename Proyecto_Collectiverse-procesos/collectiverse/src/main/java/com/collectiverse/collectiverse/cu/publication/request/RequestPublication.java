package com.collectiverse.collectiverse.cu.publication.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.List;

/** Datos que envía el cliente: cabecera de la publicación + detalle de intercambios. */
public record RequestPublication(
        @NotBlank(message = "El username es obligatorio")
        @Size(max = 50, message = "El username no puede pasar de 50 caracteres")
        String username,
        @NotNull(message = "El producto es obligatorio")
        Integer productId,
        @NotBlank(message = "El título es obligatorio")
        @Size(max = 100, message = "El título no puede pasar de 100 caracteres")
        String title,
        @Size(max = 255, message = "La descripción no puede pasar de 255 caracteres")
        String description,
        @NotBlank(message = "El tipo es obligatorio")
        @Pattern(regexp = "VENTA|INTERCAMBIO", message = "El tipo debe ser VENTA o INTERCAMBIO")
        String type,
        @NotBlank(message = "El estado es obligatorio")
        @Size(max = 20, message = "El estado no puede pasar de 20 caracteres")
        String state,
        @NotBlank(message = "createdBy es obligatorio")
        @Size(max = 50, message = "createdBy no puede pasar de 50 caracteres")
        String createdBy,
        @Valid List<RequestPublicationExchange> exchanges) { // obligatorio solo si type = INTERCAMBIO

    /** Artículo que el publicador pide a cambio. */
    public record RequestPublicationExchange(
            @NotBlank(message = "El nombre solicitado es obligatorio")
            @Size(max = 100, message = "El nombre solicitado no puede pasar de 100 caracteres")
            String requestedName,
            @Size(max = 255, message = "La descripción del intercambio no puede pasar de 255 caracteres")
            String description,
            @NotNull(message = "La cantidad es obligatoria")
            @Min(value = 1, message = "La cantidad debe ser al menos 1")
            Integer quantity) {
    }
}
