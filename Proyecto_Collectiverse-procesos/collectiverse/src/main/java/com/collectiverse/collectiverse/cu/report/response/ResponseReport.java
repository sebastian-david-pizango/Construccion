package com.collectiverse.collectiverse.cu.report.response;

import java.time.LocalDateTime;

/** Datos que se devuelven al cliente (nunca se expone la entidad completa). */
public record ResponseReport(Integer id, String username, Integer idPublication, String reason,
        String state, LocalDateTime date, Boolean status) {
}
