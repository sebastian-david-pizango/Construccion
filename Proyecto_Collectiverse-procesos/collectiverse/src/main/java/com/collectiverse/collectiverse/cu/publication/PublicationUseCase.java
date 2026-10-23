package com.collectiverse.collectiverse.cu.publication;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.collectiverse.collectiverse.cu.product.exception.ProductNotFoundException;
import com.collectiverse.collectiverse.cu.publication.exception.PublicationNotFoundException;
import com.collectiverse.collectiverse.cu.publication.request.RequestPublication;
import com.collectiverse.collectiverse.cu.publication.request.RequestPublication.RequestPublicationExchange;
import com.collectiverse.collectiverse.cu.publication.response.ResponsePublication;
import com.collectiverse.collectiverse.cu.publication.response.ResponsePublication.ResponsePublicationExchange;
import com.collectiverse.collectiverse.cu.shared.exception.BadRequestException;
import com.collectiverse.collectiverse.dominio.entity.Product;
import com.collectiverse.collectiverse.dominio.entity.Publication;
import com.collectiverse.collectiverse.dominio.repository.RepoProduct;
import com.collectiverse.collectiverse.dominio.repository.RepoPublication;

/** Caso de uso: registrar y consultar una publicación de venta o intercambio. */
@Service
public class PublicationUseCase {

    private static final String VENTA = "VENTA";
    private static final String INTERCAMBIO = "INTERCAMBIO";

    private final RepoPublication repository;
    private final RepoProduct repoProduct;

    public PublicationUseCase(RepoPublication repository, RepoProduct repoProduct) {
        this.repository = repository;
        this.repoProduct = repoProduct;
    }

    @Transactional(readOnly = true)
    public List<ResponsePublication> listar() {
        return repository.findAll().stream().map(this::aResponse).toList();
    }

    @Transactional(readOnly = true)
    public ResponsePublication consultar(Integer id) {
        return aResponse(repository.findById(id)
                .orElseThrow(() -> new PublicationNotFoundException(id)));
    }

    // Query 1: publicaciones activas de un usuario (Publication + Product + Category + Exchange)
    @Transactional(readOnly = true)
    public List<ResponsePublication> buscarPorUsername(String username) {
        if (username == null || username.isBlank()) {
            throw new BadRequestException("El username es obligatorio");
        }
        return repository.findActiveByUsernameWithDetails(username.trim()).stream().map(this::aResponse).toList();
    }

    // Query 2: publicaciones activas por tipo (Publication + Product + Category + Exchange)
    @Transactional(readOnly = true)
    public List<ResponsePublication> buscarPorTipo(String type) {
        if (!VENTA.equals(type) && !INTERCAMBIO.equals(type)) {
            throw new BadRequestException("El tipo debe ser VENTA o INTERCAMBIO");
        }
        return repository.findActiveByTypeWithDetails(type).stream().map(this::aResponse).toList();
    }

    @Transactional
    public ResponsePublication registrar(RequestPublication request) {
        boolean esIntercambio = INTERCAMBIO.equals(request.type());
        // Un INTERCAMBIO debe pedir al menos un artículo a cambio
        if (esIntercambio && (request.exchanges() == null || request.exchanges().isEmpty())) {
            throw new BadRequestException("Una publicación de INTERCAMBIO debe incluir al menos un artículo solicitado");
        }
        // El producto publicado debe existir
        Product product = repoProduct.findById(request.productId())
                .orElseThrow(() -> new ProductNotFoundException(request.productId()));
        Publication p = new Publication();
        p.setUsername(request.username()); // dueño de la publicación
        p.setProduct(product);
        p.setTitle(request.title());
        p.setDescription(request.description());
        p.setType(request.type());
        p.setState(request.state());
        p.setCreatedBy(request.createdBy());
        // Detalle: solo las publicaciones de INTERCAMBIO guardan artículos solicitados
        if (esIntercambio) {
            for (RequestPublicationExchange item : request.exchanges()) {
                p.addExchange(item.requestedName(), item.description(), item.quantity());
            }
            // Los detalles nuevos también necesitan createdBy (campo obligatorio)
            p.getExchanges().forEach(x -> x.setCreatedBy(request.createdBy()));
        }
        return aResponse(repository.save(p));
    }

    // Convierte la entidad en el Response que ve el cliente
    private ResponsePublication aResponse(Publication e) {
        return new ResponsePublication(e.getId(), e.getUsername(), e.getProduct().getId(),
                e.getProduct().getName(), e.getProduct().getCategory().getName(),
                e.getTitle(), e.getDescription(), e.getType(), e.getState(), e.getDate(),
                e.getExchanges().stream()
                        .map(x -> new ResponsePublicationExchange(x.getId(), x.getRequestedName(),
                                x.getDescription(), x.getQuantity()))
                        .toList(),
                e.getStatus());
    }
}
