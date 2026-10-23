package com.collectiverse.collectiverse.dominio.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.collectiverse.collectiverse.dominio.entity.Publication;

public interface RepoPublication extends JpaRepository<Publication, Integer> {

    // QUERY 1: Publication + Product + Category + (LEFT JOIN) PublicationExchange
    // Publicaciones activas de un usuario con todos sus datos relacionados.
    @Query("select distinct p from Publication p "
            + "join fetch p.product pr "
            + "join fetch pr.category c "
            + "left join fetch p.exchanges e "
            + "where p.username = :username and p.status = true "
            + "order by p.date desc")
    List<Publication> findActiveByUsernameWithDetails(@Param("username") String username);

    // QUERY 2: Publication + Product + Category + (LEFT JOIN) PublicationExchange
    // Publicaciones activas de un tipo (VENTA / INTERCAMBIO) con todos sus datos relacionados.
    @Query("select distinct p from Publication p "
            + "join fetch p.product pr "
            + "join fetch pr.category c "
            + "left join fetch p.exchanges e "
            + "where p.type = :type and p.status = true "
            + "order by p.date desc")
    List<Publication> findActiveByTypeWithDetails(@Param("type") String type);
}
