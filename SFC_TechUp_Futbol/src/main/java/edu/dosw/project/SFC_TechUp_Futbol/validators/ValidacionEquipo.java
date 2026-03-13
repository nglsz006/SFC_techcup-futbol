package edu.dosw.project.SFC_TechUp_Futbol.validators;

import java.util.Map;

public class ValidacionEquipo implements Validacion {
    @Override
    public void validar(Map<String, Object> datos) {
        String nombre = (String) datos.get("nombre");
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del equipo es obligatorio");
        }

        if (nombre.length() < 3) {
            throw new IllegalArgumentException("El nombre debe tener al menos 3 caracteres");
        }

        String colorPrincipal = (String) datos.get("colorPrincipal");
        if (colorPrincipal == null || colorPrincipal.isBlank()) {
            throw new IllegalArgumentException("El color principal es obligatorio");
        }
    }
}
