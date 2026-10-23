package com.collectiverse.collectiverse.cu.productImage;

import java.security.Principal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import com.collectiverse.collectiverse.cu.productImage.request.RequestProductImage;
import com.collectiverse.collectiverse.cu.productImage.response.ResponseProductImage;

/** Endpoints del caso de uso para ProductImage. */
@RestController
@RequestMapping("/api/product-image")
public class ProductImageController {

    private final ProductImageUseCase useCase;

    public ProductImageController(ProductImageUseCase useCase) {
        this.useCase = useCase;
    }

    // GET /api/product-image -> lista todos
    @GetMapping
    public List<ResponseProductImage> listar() {
        return useCase.listar();
    }

    // GET /api/product-image/{id} -> consulta uno (404 si no existe)
    @GetMapping("/{id}")
    public ResponseProductImage consultar(@PathVariable Integer id) {
        return useCase.consultar(id);
    }

    // POST /api/product-image -> registra (@Valid activa las validaciones del Request)
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseProductImage registrar(@Valid @RequestBody RequestProductImage request, Principal principal) {
        // El usuario sale de la sesión autenticada, no del body
        return useCase.registrar(request, principal.getName());
    }
}
