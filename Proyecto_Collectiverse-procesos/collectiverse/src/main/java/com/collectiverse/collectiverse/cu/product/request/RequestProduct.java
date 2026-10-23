package com.collectiverse.collectiverse.cu.product.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;

/** Datos que envía el cliente: cabecera del producto + detalle de imágenes. */
public record RequestProduct(
        @NotNull(message = "La categoría es obligatoria")
        Integer categoryId,
        Integer collectionId, // opcional: un producto puede no pertenecer a una colección
        @NotBlank(message = "El nombre del producto es obligatorio")
        @Size(max = 100, message = "El nombre no puede pasar de 100 caracteres")
        String name,
        @NotNull(message = "El stock es obligatorio")
        @Min(value = 0, message = "El stock no puede ser negativo")
        Integer stock,
        @NotNull(message = "El precio es obligatorio")
        @DecimalMin(value = "0.0", message = "El precio no puede ser negativo")
        BigDecimal price,
        @NotBlank(message = "createdBy es obligatorio")
        @Size(max = 50, message = "createdBy no puede pasar de 50 caracteres")
        String createdBy,
        @Valid List<RequestImage> images) { // cero o más imágenes

    /** Imagen que acompaña al producto al registrarlo. */
    public record RequestImage(
            @NotBlank(message = "La URL de la imagen es obligatoria")
            @Size(max = 255, message = "La URL no puede pasar de 255 caracteres")
            String url,
            Integer order) {
    }
}
