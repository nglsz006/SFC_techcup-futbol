package edu.dosw.project.SFC_TechUp_Futbol.core.validator;

import org.springframework.stereotype.Component;

@Component
public class PagoValidator {

    public void validarSubirComprobante(String equipoId, String comprobante) {
        if (equipoId == null || equipoId.isBlank()) {
            throw new IllegalArgumentException("El id del equipo es obligatorio.");
        }
        if (comprobante == null || comprobante.isBlank()) {
            throw new IllegalArgumentException("El comprobante no puede estar vacío.");
        }
        if (comprobante.length() > 500) {
            throw new IllegalArgumentException("La URL del comprobante es demasiado larga.");
        }
    }
}
