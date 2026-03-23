package edu.dosw.project.SFC_TechUp_Futbol;

import edu.dosw.project.SFC_TechUp_Futbol.core.validator.ValidacionEquipo;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    void validarComposicion_menosDeSiete_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> validacion.validarComposicionEquipo(List.of(1, 2, 3)));
    }

    @Test
    void validarComposicion_masDe12_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () ->
                validacion.validarComposicionEquipo(List.of(1,2,3,4,5,6,7,8,9,10,11,12,13)));
    }

    @Test
    void validarComposicion_valido_noLanzaExcepcion() {
        assertDoesNotThrow(() -> validacion.validarComposicionEquipo(List.of(1,2,3,4,5,6,7)));
    }
}
