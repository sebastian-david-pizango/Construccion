package com.collectiverse.collectiverse.cu.product.exception;

import com.collectiverse.collectiverse.cu.shared.exception.ResourceNotFoundException;

/** Se lanza cuando no existe el producto con el id indicado. */
public class ProductNotFoundException extends ResourceNotFoundException {

    public ProductNotFoundException(Integer id) {
        super("El producto no encontrado con id: " + id);
    }
}
