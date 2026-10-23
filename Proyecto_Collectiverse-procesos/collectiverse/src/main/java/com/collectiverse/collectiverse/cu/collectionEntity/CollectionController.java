package com.collectiverse.collectiverse.cu.collectionEntity;

import java.security.Principal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import com.collectiverse.collectiverse.cu.collectionEntity.request.RequestCollection;
import com.collectiverse.collectiverse.cu.collectionEntity.response.ResponseCollection;

/** Endpoints del caso de uso para CollectionEntity. */
@RestController
@RequestMapping("/api/collection")
public class CollectionController {

    private final CollectionUseCase useCase;

    public CollectionController(CollectionUseCase useCase) {
        this.useCase = useCase;
    }

    // GET /api/collection -> lista todos
    @GetMapping
    public List<ResponseCollection> listar() {
        return useCase.listar();
    }

    // GET /api/collection/{id} -> consulta uno (404 si no existe)
    @GetMapping("/{id}")
    public ResponseCollection consultar(@PathVariable Integer id) {
        return useCase.consultar(id);
    }

    // POST /api/collection -> registra (@Valid activa las validaciones del Request)
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseCollection registrar(@Valid @RequestBody RequestCollection request, Principal principal) {
        // El usuario sale de la sesión autenticada, no del body
        return useCase.registrar(request, principal.getName());
    }
}
