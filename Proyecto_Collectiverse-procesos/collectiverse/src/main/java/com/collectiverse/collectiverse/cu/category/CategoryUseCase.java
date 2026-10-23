package com.collectiverse.collectiverse.cu.category;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.collectiverse.collectiverse.cu.category.exception.CategoryNotFoundException;
import com.collectiverse.collectiverse.cu.category.request.RequestCategory;
import com.collectiverse.collectiverse.cu.category.response.ResponseCategory;
import com.collectiverse.collectiverse.dominio.entity.Category;
import com.collectiverse.collectiverse.dominio.repository.RepoCategory;

/** Caso de uso: consultar y registrar Category. */
@Service
public class CategoryUseCase {

    private final RepoCategory repository;

    public CategoryUseCase(RepoCategory repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<ResponseCategory> listar() {
        return repository.findAll().stream().map(this::aResponse).toList();
    }

    @Transactional(readOnly = true)
    public ResponseCategory consultar(Integer id) {
        return aResponse(repository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(id)));
    }

    @Transactional
    public ResponseCategory registrar(RequestCategory request, String usuario) {
        Category c = new Category();
        c.setName(request.name());
        c.setDescription(request.description());
        c.setCreatedBy(usuario); // quién la creó
        return aResponse(repository.save(c));
    }

    // Convierte la entidad en el Response que ve el cliente
    private ResponseCategory aResponse(Category e) {
        return new ResponseCategory(e.getId(), e.getName(), e.getDescription(), e.getStatus());
    }
}
