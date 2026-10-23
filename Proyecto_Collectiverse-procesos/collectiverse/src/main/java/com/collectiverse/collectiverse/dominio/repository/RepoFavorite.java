package com.collectiverse.collectiverse.dominio.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.collectiverse.collectiverse.dominio.entity.Favorite;

public interface RepoFavorite extends JpaRepository<Favorite, Integer> {
    // cambio de UserId a username
    boolean existsByUsernameAndPublicationId(String username, Integer publicationId);
}
