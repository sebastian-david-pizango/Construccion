package com.collectiverse.collectiverse.cu.product;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import com.collectiverse.collectiverse.cu.product.request.RequestProduct;
import com.collectiverse.collectiverse.cu.product.response.ResponseProduct;

/** Endpoints del caso de uso "Registrar y consultar un producto del catálogo" (públicos, ver SecurityConfig). */
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductUseCase useCase;

    public ProductController(ProductUseCase useCase) {
        this.useCase = useCase;
    }

    // GET /api/products -> lista todos
    @GetMapping
    public List<ResponseProduct> listar() {
        return useCase.listar();
    }

    // GET /api/products/search?categoryId=1  o  ?term=manga -> Query 1 / Query 2 (400 sin filtro)
    // (se declara antes que /{id} para que "search" no se interprete como un id)
    @GetMapping("/search")
    public List<ResponseProduct> buscar(@RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) String term) {
        return useCase.buscar(categoryId, term);
    }

    // GET /api/products/{id} -> consulta uno con sus relaciones (404 si no existe)
    @GetMapping("/{id}")
    public ResponseProduct consultar(@PathVariable Integer id) {
        return useCase.consultar(id);
    }

    // POST /api/products -> registra producto + imágenes (@Valid activa las validaciones del Request)
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseProduct registrar(@Valid @RequestBody RequestProduct request) {
        return useCase.registrar(request);
    }
}
