package com.collectiverse.collectiverse.cu.rating;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.collectiverse.collectiverse.cu.publication.exception.PublicationNotFoundException;
import com.collectiverse.collectiverse.cu.rating.exception.DuplicateRatingException;
import com.collectiverse.collectiverse.cu.rating.exception.RatingNotFoundException;
import com.collectiverse.collectiverse.cu.rating.request.RequestRating;
import com.collectiverse.collectiverse.cu.rating.response.ResponseRating;
import com.collectiverse.collectiverse.dominio.entity.Publication;
import com.collectiverse.collectiverse.dominio.entity.Rating;
import com.collectiverse.collectiverse.dominio.repository.RepoPublication;
import com.collectiverse.collectiverse.dominio.repository.RepoRating;

/** Caso de uso: consultar y registrar Rating. */
@Service
public class RatingUseCase {

    private final RepoRating repository;
    private final RepoPublication repoPublication;

    public RatingUseCase(RepoRating repository, RepoPublication repoPublication) {
        this.repository = repository;
        this.repoPublication = repoPublication;
    }

    @Transactional(readOnly = true)
    public List<ResponseRating> listar() {
        return repository.findAll().stream().map(this::aResponse).toList();
    }

    @Transactional(readOnly = true)
    public ResponseRating consultar(Integer id) {
        return aResponse(repository.findById(id)
                .orElseThrow(() -> new RatingNotFoundException(id)));
    }

    @Transactional
    public ResponseRating registrar(RequestRating request, String usuario) {
        // La publicación debe existir
        Publication publication = repoPublication.findById(request.idPublication())
                .orElseThrow(() -> new PublicationNotFoundException(request.idPublication()));
        // Un usuario califica una publicación una sola vez
        if (repository.existsByUsernameAndPublicationId(usuario, request.idPublication())) {
            throw new DuplicateRatingException("Ya calificaste la publicación " + request.idPublication());
        }
        Rating r = new Rating();
        r.setUsername(usuario);
        r.setPublication(publication);
        r.setUserRating(request.userRating());
        r.setPublicationRating(request.publicationRating());
        r.setComment(request.comment());
        r.setCreatedBy(usuario);
        return aResponse(repository.save(r));
    }

    // Convierte la entidad en el Response que ve el cliente
    private ResponseRating aResponse(Rating e) {
        return new ResponseRating(e.getId(), e.getUsername(), e.getPublication().getId(), e.getUserRating(),
                e.getPublicationRating(), e.getComment(), e.getDate(), e.getStatus());
    }
}
