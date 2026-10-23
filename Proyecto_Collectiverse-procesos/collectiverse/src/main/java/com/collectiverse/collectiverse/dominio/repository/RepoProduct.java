package com.collectiverse.collectiverse.dominio.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.collectiverse.collectiverse.dominio.entity.Product;

public interface RepoProduct extends JpaRepository<Product, Integer> {

    // QUERY 1: Product + Category + (LEFT JOIN) CollectionEntity + (LEFT JOIN) ProductImage
    // Productos activos de una categoría con sus datos relacionados.
    @Query("select distinct p from Product p "
            + "join fetch p.category c "
            + "left join fetch p.collection col "
            + "left join fetch p.images i "
            + "where c.id = :categoryId and p.status = true "
            + "order by p.name asc")
    List<Product> findActiveByCategoryWithDetails(@Param("categoryId") Integer categoryId);

    // QUERY 2: Product + Category + (LEFT JOIN) CollectionEntity + (LEFT JOIN) ProductImage
    // Productos activos cuyo nombre contiene el término (sin distinguir mayúsculas).
    @Query("select distinct p from Product p "
            + "join fetch p.category c "
            + "left join fetch p.collection col "
            + "left join fetch p.images i "
            + "where lower(p.name) like lower(concat('%', :term, '%')) and p.status = true "
            + "order by p.name asc")
    List<Product> searchActiveWithDetails(@Param("term") String term);
}
