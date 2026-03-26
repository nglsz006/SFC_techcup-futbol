package edu.dosw.project.SFC_TechUp_Futbol.core.validator;

import edu.dosw.project.SFC_TechUp_Futbol.controller.dto.request.ConsultaAuditoriaRequest;
import edu.dosw.project.SFC_TechUp_Futbol.core.exception.FiltroAuditoriaInvalidoException;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.TipoAccionAuditoria;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.stream.Collectors;

@Component
public class AuditoriaValidator {

    public void validarConsulta(ConsultaAuditoriaRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("La consulta de auditoria es obligatoria.");
        }
        if (request.getUsuario() != null && request.getUsuario().isBlank()) {
            throw new FiltroAuditoriaInvalidoException("El filtro de usuario no puede estar vacio.");
        }
        if (request.getTipoAccion() != null && !request.getTipoAccion().isBlank() && !tipoAccionValido(request.getTipoAccion())) {
            throw new FiltroAuditoriaInvalidoException(
                    "El tipo de accion no es valido. Valores permitidos: " + accionesPermitidas() + "."
            );
        }
        if (request.getFechaDesde() != null && request.getFechaHasta() != null
                && request.getFechaDesde().isAfter(request.getFechaHasta())) {
            throw new FiltroAuditoriaInvalidoException("El rango de fechas es inconsistente.");
        }
    }

    private boolean tipoAccionValido(String tipoAccion) {
        return Arrays.stream(TipoAccionAuditoria.values())
                .anyMatch(valor -> valor.name().equalsIgnoreCase(tipoAccion.trim()));
    }

    private String accionesPermitidas() {
        return Arrays.stream(TipoAccionAuditoria.values())
                .map(Enum::name)
                .collect(Collectors.joining(", "));
    }
}
