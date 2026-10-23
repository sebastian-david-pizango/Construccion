package com.collectiverse.collectiverse.cu.report.exception;

import com.collectiverse.collectiverse.cu.shared.exception.ResourceNotFoundException;

/** Se lanza cuando no existe el reporte con el id indicado. */
public class ReportNotFoundException extends ResourceNotFoundException {

    public ReportNotFoundException(Integer id) {
        super("El reporte no encontrado con id: " + id);
    }
}
