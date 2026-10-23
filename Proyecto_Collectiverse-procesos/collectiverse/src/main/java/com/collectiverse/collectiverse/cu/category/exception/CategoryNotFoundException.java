package com.collectiverse.collectiverse.cu.category.exception;

import com.collectiverse.collectiverse.cu.shared.exception.ResourceNotFoundException;

/** Se lanza cuando no existe la categoría con el id indicado. */
public class CategoryNotFoundException extends ResourceNotFoundException {

    public CategoryNotFoundException(Integer id) {
        super("La categoría no encontrado con id: " + id);
    }
}
