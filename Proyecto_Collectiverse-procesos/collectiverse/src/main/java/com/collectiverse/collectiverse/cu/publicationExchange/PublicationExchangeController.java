package com.collectiverse.collectiverse.cu.publicationExchange;

import java.security.Principal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import com.collectiverse.collectiverse.cu.publicationExchange.request.RequestPublicationExchange;
import com.collectiverse.collectiverse.cu.publicationExchange.response.ResponsePublicationExchange;

/** Endpoints del caso de uso para PublicationExchange. */
@RestController
@RequestMapping("/api/publication-exchange")
public class PublicationExchangeController {

    private final PublicationExchangeUseCase useCase;

    public PublicationExchangeController(PublicationExchangeUseCase useCase) {
        this.useCase = useCase;
    }

    // GET /api/publication-exchange -> lista todos
    @GetMapping
    public List<ResponsePublicationExchange> listar() {
        return useCase.listar();
    }

    // GET /api/publication-exchange/{id} -> consulta uno (404 si no existe)
    @GetMapping("/{id}")
    public ResponsePublicationExchange consultar(@PathVariable Integer id) {
        return useCase.consultar(id);
    }

    // POST /api/publication-exchange -> registra (@Valid activa las validaciones del Request)
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponsePublicationExchange registrar(@Valid @RequestBody RequestPublicationExchange request, Principal principal) {
        // El usuario sale de la sesión autenticada, no del body
        return useCase.registrar(request, principal.getName());
    }
}
