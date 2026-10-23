package com.collectiverse.collectiverse.cu.category;

import java.security.Principal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import com.collectiverse.collectiverse.cu.category.request.RequestCategory;
import com.collectiverse.collectiverse.cu.category.response.ResponseCategory;

/** Endpoints del caso de uso para Category. */
@RestController
@RequestMapping("/api/category")
public class CategoryController {

    private final CategoryUseCase useCase;

    public CategoryController(CategoryUseCase useCase) {
        this.useCase = useCase;
    }

    // GET /api/category -> lista todos
    @GetMapping
    public List<ResponseCategory> listar() {
        return useCase.listar();
    }

    // GET /api/category/{id} -> consulta uno (404 si no existe)
    @GetMapping("/{id}")
    public ResponseCategory consultar(@PathVariable Integer id) {
        return useCase.consultar(id);
    }

    // POST /api/category -> registra (@Valid activa las validaciones del Request)
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseCategory registrar(@Valid @RequestBody RequestCategory request, Principal principal) {
        // El usuario sale de la sesión autenticada, no del body
        return useCase.registrar(request, principal.getName());
    }
}
