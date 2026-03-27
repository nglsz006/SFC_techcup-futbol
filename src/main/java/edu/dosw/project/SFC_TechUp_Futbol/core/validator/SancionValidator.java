package edu.dosw.project.SFC_TechUp_Futbol.core.validator;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Sancion;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SancionValidator {

    public void validarSancion(Sancion sancion) {
        if (sancion == null) {
            throw new IllegalArgumentException("La sanción no puede ser nula.");
        }
        if (sancion.getTipoSancion() == null) {
            throw new IllegalArgumentException("El tipo de sanción es obligatorio.");
        }
        if (sancion.getDescripcion() == null || sancion.getDescripcion().isBlank()) {
            throw new IllegalArgumentException("La descripción de la sanción es obligatoria.");
        }
    }

    public void validarListaSanciones(List<Sancion> sanciones) {
        if (sanciones == null) {
            throw new IllegalArgumentException("La lista de sanciones no puede ser nula.");
        }
        sanciones.stream().forEach(this::validarSancion);
    }

    public boolean esValida(Sancion sancion) {
        return sancion != null
                && sancion.getTipoSancion() != null
                && sancion.getDescripcion() != null
                && !sancion.getDescripcion().isBlank();
    }
}