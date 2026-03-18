package edu.dosw.project.SFC_TechUp_Futbol;

import edu.dosw.project.SFC_TechUp_Futbol.core.validator.ValidacionEquipo;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ValidacionEquipoTest {

    private final ValidacionEquipo validacion = new ValidacionEquipo();

    private Map<String, Object> datosValidos() {
        Map<String, Object> datos = new HashMap<>();
        datos.put("nombre", "Los Tigres");
        datos.put("colorPrincipal", "rojo");
        return datos;
    }

    @Test
    void validar_datosCorrectos_noLanzaExcepcion() {
        assertDoesNotThrow(() -> validacion.validar(datosValidos()));
    }

    @Test
    void validar_sinNombre_lanzaExcepcion() {
        Map<String, Object> datos = datosValidos();
        datos.put("nombre", "");
        assertThrows(IllegalArgumentException.class, () -> validacion.validar(datos));
    }

    @Test
    void validar_nombreMuyCorto_lanzaExcepcion() {
        Map<String, Object> datos = datosValidos();
        datos.put("nombre", "AB");
        assertThrows(IllegalArgumentException.class, () -> validacion.validar(datos));
    }

    @Test
    void validar_sinColorPrincipal_lanzaExcepcion() {
        Map<String, Object> datos = datosValidos();
        datos.put("colorPrincipal", "");
        assertThrows(IllegalArgumentException.class, () -> validacion.validar(datos));
    }
}
