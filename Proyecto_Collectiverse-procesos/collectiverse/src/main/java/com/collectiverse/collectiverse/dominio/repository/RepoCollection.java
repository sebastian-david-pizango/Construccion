package com.collectiverse.collectiverse.dominio.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.collectiverse.collectiverse.dominio.entity.CollectionEntity;

public interface RepoCollection extends JpaRepository<CollectionEntity, Integer> {
}