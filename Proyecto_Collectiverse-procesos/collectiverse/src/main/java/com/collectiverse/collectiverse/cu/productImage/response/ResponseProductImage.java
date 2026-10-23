package com.collectiverse.collectiverse.cu.productImage.response;

/** Datos que se devuelven al cliente (nunca se expone la entidad completa). */
public record ResponseProductImage(Integer id, Integer idProduct, String url, Integer order, Boolean status) {
}
