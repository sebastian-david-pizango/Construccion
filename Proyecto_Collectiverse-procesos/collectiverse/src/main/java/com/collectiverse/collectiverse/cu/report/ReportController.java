package com.collectiverse.collectiverse.cu.report;

import java.security.Principal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import com.collectiverse.collectiverse.cu.report.request.RequestReport;
import com.collectiverse.collectiverse.cu.report.response.ResponseReport;

/** Endpoints del caso de uso para Report. */
@RestController
@RequestMapping("/api/report")
public class ReportController {

    private final ReportUseCase useCase;

    public ReportController(ReportUseCase useCase) {
        this.useCase = useCase;
    }

    // GET /api/report -> lista todos
    @GetMapping
    public List<ResponseReport> listar() {
        return useCase.listar();
    }

    // GET /api/report/{id} -> consulta uno (404 si no existe)
    @GetMapping("/{id}")
    public ResponseReport consultar(@PathVariable Integer id) {
        return useCase.consultar(id);
    }

    // POST /api/report -> registra (@Valid activa las validaciones del Request)
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseReport registrar(@Valid @RequestBody RequestReport request, Principal principal) {
        // El usuario sale de la sesión autenticada, no del body
        return useCase.registrar(request, principal.getName());
    }
}
