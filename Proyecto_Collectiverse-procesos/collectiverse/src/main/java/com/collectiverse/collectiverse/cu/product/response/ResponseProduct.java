package com.collectiverse.collectiverse.cu.product.response;

import java.math.BigDecimal;
import java.util.List;

/** Datos que se devuelven al cliente (nunca se expone la entidad completa). */
public record ResponseProduct(Integer id, Integer categoryId, String categoryName,
        Integer collectionId, String collectionName, String name, Integer stock, BigDecimal price,
        List<ResponseImage> images, Boolean status) {

    public record ResponseImage(Integer id, String url, Integer order) {
    }
}
