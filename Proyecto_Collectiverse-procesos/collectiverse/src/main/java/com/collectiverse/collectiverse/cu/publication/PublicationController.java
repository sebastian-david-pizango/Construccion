package com.collectiverse.collectiverse.cu.publication;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import com.collectiverse.collectiverse.cu.publication.request.RequestPublication;
import com.collectiverse.collectiverse.cu.publication.response.ResponsePublication;

/** Endpoints del caso de uso "Registrar y consultar una publicación" (públicos, ver SecurityConfig). */
@RestController
@RequestMapping("/api/publications")
public class PublicationController {

    private final PublicationUseCase useCase;

    public PublicationController(PublicationUseCase useCase) {
        this.useCase = useCase;
    }

    // GET /api/publications -> lista todas
    @GetMapping
    public List<ResponsePublication> listar() {
        return useCase.listar();
    }

    // GET /api/publications/search?username=ana -> Query 1 (400 si falta username)
    // (se declara antes que /{id} para que "search" no se interprete como un id)
    @GetMapping("/search")
    public List<ResponsePublication> buscarPorUsername(@RequestParam(required = false) String username) {
        return useCase.buscarPorUsername(username);
    }

    // GET /api/publications/by-type?type=VENTA -> Query 2 (400 si el tipo no es válido)
    @GetMapping("/by-type")
    public List<ResponsePublication> buscarPorTipo(@RequestParam(required = false) String type) {
        return useCase.buscarPorTipo(type);
    }

    // GET /api/publications/{id} -> consulta una (404 si no existe)
    @GetMapping("/{id}")
    public ResponsePublication consultar(@PathVariable Integer id) {
        return useCase.consultar(id);
    }

    // POST /api/publications -> registra publicación + detalle de intercambios
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponsePublication registrar(@Valid @RequestBody RequestPublication request) {
        return useCase.registrar(request);
    }
}
