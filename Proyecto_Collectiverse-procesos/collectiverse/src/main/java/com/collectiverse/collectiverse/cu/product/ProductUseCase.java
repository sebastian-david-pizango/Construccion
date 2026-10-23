package com.collectiverse.collectiverse.cu.product;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.collectiverse.collectiverse.cu.category.exception.CategoryNotFoundException;
import com.collectiverse.collectiverse.cu.collectionEntity.exception.CollectionNotFoundException;
import com.collectiverse.collectiverse.cu.product.exception.ProductNotFoundException;
import com.collectiverse.collectiverse.cu.product.request.RequestProduct;
import com.collectiverse.collectiverse.cu.product.request.RequestProduct.RequestImage;
import com.collectiverse.collectiverse.cu.product.response.ResponseProduct;
import com.collectiverse.collectiverse.cu.product.response.ResponseProduct.ResponseImage;
import com.collectiverse.collectiverse.cu.shared.exception.BadRequestException;
import com.collectiverse.collectiverse.dominio.entity.Category;
import com.collectiverse.collectiverse.dominio.entity.CollectionEntity;
import com.collectiverse.collectiverse.dominio.entity.Product;
import com.collectiverse.collectiverse.dominio.repository.RepoCategory;
import com.collectiverse.collectiverse.dominio.repository.RepoCollection;
import com.collectiverse.collectiverse.dominio.repository.RepoProduct;

/** Caso de uso: registrar y consultar un producto del catálogo (con sus imágenes). */
@Service
public class ProductUseCase {

    private final RepoProduct repository;
    private final RepoCategory repoCategory;
    private final RepoCollection repoCollection;

    public ProductUseCase(RepoProduct repository, RepoCategory repoCategory, RepoCollection repoCollection) {
        this.repository = repository;
        this.repoCategory = repoCategory;
        this.repoCollection = repoCollection;
    }

    @Transactional(readOnly = true)
    public List<ResponseProduct> listar() {
        return repository.findAll().stream().map(this::aResponse).toList();
    }

    @Transactional(readOnly = true)
    public ResponseProduct consultar(Integer id) {
        return aResponse(repository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id)));
    }

    // Query 1 (por categoría) y Query 2 (por término). Sin ningún filtro -> 400.
    @Transactional(readOnly = true)
    public List<ResponseProduct> buscar(Integer categoryId, String term) {
        boolean hayTermino = term != null && !term.isBlank();
        if (categoryId == null && !hayTermino) {
            throw new BadRequestException("Debe indicar categoryId o term");
        }
        List<Product> resultado;
        if (categoryId != null) {
            resultado = repository.findActiveByCategoryWithDetails(categoryId);
            if (hayTermino) { // si vienen ambos filtros, se aplican los dos
                String t = term.trim().toLowerCase();
                resultado = resultado.stream().filter(p -> p.getName().toLowerCase().contains(t)).toList();
            }
        } else {
            resultado = repository.searchActiveWithDetails(term.trim());
        }
        return resultado.stream().map(this::aResponse).toList();
    }

    @Transactional
    public ResponseProduct registrar(RequestProduct request) {
        // La categoría debe existir
        Category category = repoCategory.findById(request.categoryId())
                .orElseThrow(() -> new CategoryNotFoundException(request.categoryId()));
        Product p = new Product();
        p.setCategory(category);
        // La colección es opcional, pero si se envía debe existir
        if (request.collectionId() != null) {
            CollectionEntity collection = repoCollection.findById(request.collectionId())
                    .orElseThrow(() -> new CollectionNotFoundException(request.collectionId()));
            p.setCollection(collection);
        }
        p.setName(request.name());
        p.setStock(request.stock());
        p.setPrice(request.price());
        p.setCreatedBy(request.createdBy());
        // Detalle: las imágenes se guardan junto con el producto (cascade)
        if (request.images() != null) {
            for (RequestImage img : request.images()) {
                p.addImage(img.url(), img.order());
            }
        }
        // Las imágenes nuevas también necesitan createdBy (campo obligatorio)
        p.getImages().forEach(i -> i.setCreatedBy(request.createdBy()));
        return aResponse(repository.save(p));
    }

    // Convierte la entidad en el Response que ve el cliente
    private ResponseProduct aResponse(Product e) {
        CollectionEntity col = e.getCollection();
        return new ResponseProduct(e.getId(), e.getCategory().getId(), e.getCategory().getName(),
                col != null ? col.getId() : null, col != null ? col.getName() : null,
                e.getName(), e.getStock(), e.getPrice(),
                e.getImages().stream()
                        .map(i -> new ResponseImage(i.getId(), i.getUrl(), i.getOrder()))
                        .toList(),
                e.getStatus());
    }
}
