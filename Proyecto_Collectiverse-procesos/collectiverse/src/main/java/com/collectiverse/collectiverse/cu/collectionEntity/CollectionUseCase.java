package com.collectiverse.collectiverse.cu.collectionEntity;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.collectiverse.collectiverse.cu.category.exception.CategoryNotFoundException;
import com.collectiverse.collectiverse.cu.collectionEntity.exception.CollectionNotFoundException;
import com.collectiverse.collectiverse.cu.collectionEntity.request.RequestCollection;
import com.collectiverse.collectiverse.cu.collectionEntity.response.ResponseCollection;
import com.collectiverse.collectiverse.dominio.entity.Category;
import com.collectiverse.collectiverse.dominio.entity.CollectionEntity;
import com.collectiverse.collectiverse.dominio.repository.RepoCategory;
import com.collectiverse.collectiverse.dominio.repository.RepoCollection;

/** Caso de uso: consultar y registrar CollectionEntity. */
@Service
public class CollectionUseCase {

    private final RepoCollection repository;
    private final RepoCategory repoCategory;

    public CollectionUseCase(RepoCollection repository, RepoCategory repoCategory) {
        this.repository = repository;
        this.repoCategory = repoCategory;
    }

    @Transactional(readOnly = true)
    public List<ResponseCollection> listar() {
        return repository.findAll().stream().map(this::aResponse).toList();
    }

    @Transactional(readOnly = true)
    public ResponseCollection consultar(Integer id) {
        return aResponse(repository.findById(id)
                .orElseThrow(() -> new CollectionNotFoundException(id)));
    }

    @Transactional
    public ResponseCollection registrar(RequestCollection request, String usuario) {
        // La categoría debe existir
        Category category = repoCategory.findById(request.idCategory())
                .orElseThrow(() -> new CategoryNotFoundException(request.idCategory()));
        CollectionEntity c = new CollectionEntity();
        c.setCategory(category);
        c.setName(request.name());
        c.setDescription(request.description());
        c.setCreatedBy(usuario);
        return aResponse(repository.save(c));
    }

    // Convierte la entidad en el Response que ve el cliente
    private ResponseCollection aResponse(CollectionEntity e) {
        return new ResponseCollection(e.getId(), e.getCategory().getId(), e.getName(), e.getDescription(), e.getStatus());
    }
}
