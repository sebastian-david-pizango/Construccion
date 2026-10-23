package com.collectiverse.collectiverse.cu.rating;

import java.security.Principal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import com.collectiverse.collectiverse.cu.rating.request.RequestRating;
import com.collectiverse.collectiverse.cu.rating.response.ResponseRating;

/** Endpoints del caso de uso para Rating. */
@RestController
@RequestMapping("/api/rating")
public class RatingController {

    private final RatingUseCase useCase;

    public RatingController(RatingUseCase useCase) {
        this.useCase = useCase;
    }

    // GET /api/rating -> lista todos
    @GetMapping
    public List<ResponseRating> listar() {
        return useCase.listar();
    }

    // GET /api/rating/{id} -> consulta uno (404 si no existe)
    @GetMapping("/{id}")
    public ResponseRating consultar(@PathVariable Integer id) {
        return useCase.consultar(id);
    }

    // POST /api/rating -> registra (@Valid activa las validaciones del Request)
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseRating registrar(@Valid @RequestBody RequestRating request, Principal principal) {
        // El usuario sale de la sesión autenticada, no del body
        return useCase.registrar(request, principal.getName());
    }
}
