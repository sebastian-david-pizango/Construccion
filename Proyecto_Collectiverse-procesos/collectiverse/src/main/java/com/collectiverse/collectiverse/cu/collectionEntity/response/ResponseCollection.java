package com.collectiverse.collectiverse.cu.collectionEntity.response;

/** Datos que se devuelven al cliente (nunca se expone la entidad completa). */
public record ResponseCollection(Integer id, Integer idCategory, String name, String description, Boolean status) {
}
