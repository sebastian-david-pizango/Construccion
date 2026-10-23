package com.collectiverse.collectiverse.cu.category.response;

/** Datos que se devuelven al cliente (nunca se expone la entidad completa). */
public record ResponseCategory(Integer id, String name, String description, Boolean status) {
}
