package edu.dosw.project.SFC_TechUp_Futbol.validators;

import edu.dosw.project.SFC_TechUp_Futbol.model.Formacion;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class ValidacionStrategy {
    
    public static interface Validacion {
        void validar(Map<String, Object> datos);
    }

    public static class ValidacionTorneo implements Validacion {
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

    public static class ValidacionEquipo implements Validacion {
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

    public static class ValidacionAlineacion implements Validacion {
        @Override
        public void validar(Map<String, Object> datos) {
            String formacionStr = (String) datos.get("formacion");
            Formacion formacion = Formacion.fromString(formacionStr);
            
            if (formacion == null) {
                throw new IllegalArgumentException("Formación inválida. Use: 4-4-2, 4-3-3, 3-5-2, 4-5-1 o 5-3-2");
            }

            List<Integer> titulares = (List<Integer>) datos.get("titulares");
            if (titulares == null || titulares.size() != 11) {
                throw new IllegalArgumentException("Debe haber exactamente 11 titulares");
            }

            List<Integer> reservas = (List<Integer>) datos.get("reservas");
            if (reservas != null && reservas.size() > 7) {
                throw new IllegalArgumentException("No puede haber más de 7 reservas");
            }
        }
    }
}
