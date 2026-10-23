package com.collectiverse.collectiverse.dominio.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.collectiverse.collectiverse.dominio.entity.PublicationExchange;

public interface RepoPublicationExchange extends JpaRepository<PublicationExchange, Integer> {
}