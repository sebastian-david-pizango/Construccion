package com.collectiverse.collectiverse.cu.favorite;

import java.security.Principal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import com.collectiverse.collectiverse.cu.favorite.request.RequestFavorite;
import com.collectiverse.collectiverse.cu.favorite.response.ResponseFavorite;

/** Endpoints del caso de uso para Favorite. */
@RestController
@RequestMapping("/api/favorite")
public class FavoriteController {

    private final FavoriteUseCase useCase;

    public FavoriteController(FavoriteUseCase useCase) {
        this.useCase = useCase;
    }

    // GET /api/favorite -> lista todos
    @GetMapping
    public List<ResponseFavorite> listar() {
        return useCase.listar();
    }

    // GET /api/favorite/{id} -> consulta uno (404 si no existe)
    @GetMapping("/{id}")
    public ResponseFavorite consultar(@PathVariable Integer id) {
        return useCase.consultar(id);
    }

    // POST /api/favorite -> registra (@Valid activa las validaciones del Request)
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseFavorite registrar(@Valid @RequestBody RequestFavorite request, Principal principal) {
        // El usuario sale de la sesión autenticada, no del body
        return useCase.registrar(request, principal.getName());
    }
}
