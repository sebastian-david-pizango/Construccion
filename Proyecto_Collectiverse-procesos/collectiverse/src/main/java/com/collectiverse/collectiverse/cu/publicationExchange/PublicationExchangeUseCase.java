package com.collectiverse.collectiverse.cu.publicationExchange;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.collectiverse.collectiverse.cu.publication.exception.PublicationNotFoundException;
import com.collectiverse.collectiverse.cu.publicationExchange.exception.PublicationExchangeNotFoundException;
import com.collectiverse.collectiverse.cu.publicationExchange.request.RequestPublicationExchange;
import com.collectiverse.collectiverse.cu.publicationExchange.response.ResponsePublicationExchange;
import com.collectiverse.collectiverse.dominio.entity.Publication;
import com.collectiverse.collectiverse.dominio.entity.PublicationExchange;
import com.collectiverse.collectiverse.dominio.repository.RepoPublication;
import com.collectiverse.collectiverse.dominio.repository.RepoPublicationExchange;

/** Caso de uso: consultar y registrar PublicationExchange. */
@Service
public class PublicationExchangeUseCase {

    private final RepoPublicationExchange repository;
    private final RepoPublication repoPublication;

    public PublicationExchangeUseCase(RepoPublicationExchange repository, RepoPublication repoPublication) {
        this.repository = repository;
        this.repoPublication = repoPublication;
    }

    @Transactional(readOnly = true)
    public List<ResponsePublicationExchange> listar() {
        return repository.findAll().stream().map(this::aResponse).toList();
    }

    @Transactional(readOnly = true)
    public ResponsePublicationExchange consultar(Integer id) {
        return aResponse(repository.findById(id)
                .orElseThrow(() -> new PublicationExchangeNotFoundException(id)));
    }

    @Transactional
    public ResponsePublicationExchange registrar(RequestPublicationExchange request, String usuario) {
        // La publicación debe existir
        Publication publication = repoPublication.findById(request.idPublication())
                .orElseThrow(() -> new PublicationNotFoundException(request.idPublication()));
        PublicationExchange x = new PublicationExchange();
        x.setPublication(publication);
        x.setRequestedName(request.requestedName());
        x.setDescription(request.description());
        x.setQuantity(request.quantity());
        x.setCreatedBy(usuario);
        return aResponse(repository.save(x));
    }

    // Convierte la entidad en el Response que ve el cliente
    private ResponsePublicationExchange aResponse(PublicationExchange e) {
        return new ResponsePublicationExchange(e.getId(), e.getPublication().getId(), e.getRequestedName(),
                e.getDescription(), e.getQuantity(), e.getStatus());
    }
}
