package edu.dosw.project.SFC_TechUp_Futbol.core.validator;

import java.time.LocalDateTime;
import java.util.Map;

public class ValidacionTorneo implements Validacion {
    @Override
    public void validar(Map<String, Object> datos) {
        String nombre = (String) datos.get("nombre");
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del torneo es obligatorio");
        }

        Integer cantidadEquipos = (Integer) datos.get("cantidadEquipos");
        if (cantidadEquipos == null || cantidadEquipos < 2) {
            throw new IllegalArgumentException("Debe haber al menos 2 equipos");
        }

        Double costo = (Double) datos.get("costo");
        if (costo == null || costo < 0) {
            throw new IllegalArgumentException("El costo no puede ser negativo");
        }

        LocalDateTime fechaInicio = (LocalDateTime) datos.get("fechaInicio");
        LocalDateTime fechaFin = (LocalDateTime) datos.get("fechaFin");
        if (fechaInicio != null && fechaFin != null && !fechaInicio.isBefore(fechaFin)) {
            throw new IllegalArgumentException("La fecha de inicio debe ser anterior a la fecha de fin");
        }
    }
}

