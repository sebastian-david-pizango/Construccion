package com.collectiverse.collectiverse.cu.productImage.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/** Datos que envía el cliente en este caso de uso. */
public record RequestProductImage(
        @NotNull(message = "El producto es obligatorio")
        Integer idProduct,
        @NotBlank(message = "La URL de la imagen es obligatoria")
        @Size(max = 255, message = "La URL no puede pasar de 255 caracteres")
        String url,
        Integer order) { // posición de la imagen en la galería
}
