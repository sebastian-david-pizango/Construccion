package com.collectiverse.collectiverse.cu.productImage.exception;

import com.collectiverse.collectiverse.cu.shared.exception.ResourceNotFoundException;

/** Se lanza cuando no existe la imagen con el id indicado. */
public class ProductImageNotFoundException extends ResourceNotFoundException {

    public ProductImageNotFoundException(Integer id) {
        super("La imagen no encontrado con id: " + id);
    }
}
