package com.collectiverse.collectiverse.dominio.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.collectiverse.collectiverse.dominio.entity.ProductImage;

public interface RepoProductImage extends JpaRepository<ProductImage, Integer> {
}