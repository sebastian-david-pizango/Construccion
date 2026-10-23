package com.collectiverse.collectiverse.cu.productImage;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.collectiverse.collectiverse.cu.product.exception.ProductNotFoundException;
import com.collectiverse.collectiverse.cu.productImage.exception.ProductImageNotFoundException;
import com.collectiverse.collectiverse.cu.productImage.request.RequestProductImage;
import com.collectiverse.collectiverse.cu.productImage.response.ResponseProductImage;
import com.collectiverse.collectiverse.dominio.entity.Product;
import com.collectiverse.collectiverse.dominio.entity.ProductImage;
import com.collectiverse.collectiverse.dominio.repository.RepoProduct;
import com.collectiverse.collectiverse.dominio.repository.RepoProductImage;

/** Caso de uso: consultar y registrar ProductImage. */
@Service
public class ProductImageUseCase {

    private final RepoProductImage repository;
    private final RepoProduct repoProduct;

    public ProductImageUseCase(RepoProductImage repository, RepoProduct repoProduct) {
        this.repository = repository;
        this.repoProduct = repoProduct;
    }

    @Transactional(readOnly = true)
    public List<ResponseProductImage> listar() {
        return repository.findAll().stream().map(this::aResponse).toList();
    }

    @Transactional(readOnly = true)
    public ResponseProductImage consultar(Integer id) {
        return aResponse(repository.findById(id)
                .orElseThrow(() -> new ProductImageNotFoundException(id)));
    }

    @Transactional
    public ResponseProductImage registrar(RequestProductImage request, String usuario) {
        // El producto debe existir
        Product product = repoProduct.findById(request.idProduct())
                .orElseThrow(() -> new ProductNotFoundException(request.idProduct()));
        ProductImage img = new ProductImage();
        img.setProduct(product);
        img.setUrl(request.url());
        img.setOrder(request.order());
        img.setCreatedBy(usuario);
        return aResponse(repository.save(img));
    }

    // Convierte la entidad en el Response que ve el cliente
    private ResponseProductImage aResponse(ProductImage e) {
        return new ResponseProductImage(e.getId(), e.getProduct().getId(), e.getUrl(), e.getOrder(), e.getStatus());
    }
}
