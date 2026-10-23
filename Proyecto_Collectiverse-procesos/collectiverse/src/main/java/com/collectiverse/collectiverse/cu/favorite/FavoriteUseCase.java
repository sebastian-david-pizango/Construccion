package com.collectiverse.collectiverse.cu.favorite;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.collectiverse.collectiverse.cu.favorite.exception.FavoriteNotFoundException;
import com.collectiverse.collectiverse.cu.favorite.exception.DuplicateFavoriteException;
import com.collectiverse.collectiverse.cu.favorite.request.RequestFavorite;
import com.collectiverse.collectiverse.cu.favorite.response.ResponseFavorite;
import com.collectiverse.collectiverse.cu.publication.exception.PublicationNotFoundException;
import com.collectiverse.collectiverse.dominio.entity.Favorite;
import com.collectiverse.collectiverse.dominio.entity.Publication;
import com.collectiverse.collectiverse.dominio.repository.RepoFavorite;
import com.collectiverse.collectiverse.dominio.repository.RepoPublication;

/** Caso de uso: consultar y registrar Favorite. */
@Service
public class FavoriteUseCase {

    private final RepoFavorite repository;
    private final RepoPublication repoPublication;

    public FavoriteUseCase(RepoFavorite repository, RepoPublication repoPublication) {
        this.repository = repository;
        this.repoPublication = repoPublication;
    }

    @Transactional(readOnly = true)
    public List<ResponseFavorite> listar() {
        return repository.findAll().stream().map(this::aResponse).toList();
    }

    @Transactional(readOnly = true)
    public ResponseFavorite consultar(Integer id) {
        return aResponse(repository.findById(id)
                .orElseThrow(() -> new FavoriteNotFoundException(id)));
    }

    @Transactional
    public ResponseFavorite registrar(RequestFavorite request, String usuario) {
        // La publicación debe existir
        Publication publication = repoPublication.findById(request.idPublication())
                .orElseThrow(() -> new PublicationNotFoundException(request.idPublication()));
        // Un usuario no puede repetir el mismo favorito
        if (repository.existsByUsernameAndPublicationId(usuario, request.idPublication())) {
            throw new DuplicateFavoriteException("Ya marcaste la publicación " + request.idPublication() + " como favorita");
        }
        Favorite f = new Favorite();
        f.setUsername(usuario);
        f.setPublication(publication);
        f.setCreatedBy(usuario);
        return aResponse(repository.save(f));
    }

    // Convierte la entidad en el Response que ve el cliente
    private ResponseFavorite aResponse(Favorite e) {
        return new ResponseFavorite(e.getId(), e.getUsername(), e.getPublication().getId(), e.getDate(), e.getStatus());
    }
}
