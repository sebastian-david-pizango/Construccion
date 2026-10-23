package com.collectiverse.collectiverse.dominio.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.collectiverse.collectiverse.dominio.entity.Category;


public interface RepoCategory extends JpaRepository<Category, Integer> {
}