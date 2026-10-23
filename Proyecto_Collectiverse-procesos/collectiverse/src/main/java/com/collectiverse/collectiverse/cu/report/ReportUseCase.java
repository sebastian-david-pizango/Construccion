package com.collectiverse.collectiverse.cu.report;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.collectiverse.collectiverse.cu.publication.exception.PublicationNotFoundException;
import com.collectiverse.collectiverse.cu.report.exception.ReportNotFoundException;
import com.collectiverse.collectiverse.cu.report.request.RequestReport;
import com.collectiverse.collectiverse.cu.report.response.ResponseReport;
import com.collectiverse.collectiverse.dominio.entity.Publication;
import com.collectiverse.collectiverse.dominio.entity.Report;
import com.collectiverse.collectiverse.dominio.repository.RepoPublication;
import com.collectiverse.collectiverse.dominio.repository.RepoReport;

/** Caso de uso: consultar y registrar Report. */
@Service
public class ReportUseCase {

    private final RepoReport repository;
    private final RepoPublication repoPublication;

    public ReportUseCase(RepoReport repository, RepoPublication repoPublication) {
        this.repository = repository;
        this.repoPublication = repoPublication;
    }

    @Transactional(readOnly = true)
    public List<ResponseReport> listar() {
        return repository.findAll().stream().map(this::aResponse).toList();
    }

    @Transactional(readOnly = true)
    public ResponseReport consultar(Integer id) {
        return aResponse(repository.findById(id)
                .orElseThrow(() -> new ReportNotFoundException(id)));
    }

    @Transactional
    public ResponseReport registrar(RequestReport request, String usuario) {
        // La publicación reportada debe existir
        Publication publication = repoPublication.findById(request.idPublication())
                .orElseThrow(() -> new PublicationNotFoundException(request.idPublication()));
        Report r = new Report();
        r.setUsername(usuario);
        r.setPublication(publication);
        r.setReason(request.reason());
        r.setState("PENDIENTE"); // todo reporte nace pendiente de revisión
        r.setCreatedBy(usuario);
        return aResponse(repository.save(r));
    }

    // Convierte la entidad en el Response que ve el cliente
    private ResponseReport aResponse(Report e) {
        return new ResponseReport(e.getId(), e.getUsername(), e.getPublication().getId(), e.getReason(),
                e.getState(), e.getDate(), e.getStatus());
    }
}
