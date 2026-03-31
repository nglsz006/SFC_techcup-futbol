package edu.dosw.project.SFC_TechUp_Futbol.core.validator;

import edu.dosw.project.SFC_TechUp_Futbol.core.validator.ValidacionTorneo;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class ValidacionTorneoTest {

    private final ValidacionTorneo validacion = new ValidacionTorneo();

    private Map<String, Object> datosValidos() {
        Map<String, Object> datos = new HashMap<>();
        datos.put("nombre", "Copa DOSW");
        datos.put("cantidadEquipos", 8);
        datos.put("costo", 50.0);
        datos.put("fechaInicio", LocalDateTime.now().plusDays(1));
        datos.put("fechaFin", LocalDateTime.now().plusDays(10));
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
    void validar_unSoloEquipo_lanzaExcepcion() {
        Map<String, Object> datos = datosValidos();
        datos.put("cantidadEquipos", 1);
        assertThrows(IllegalArgumentException.class, () -> validacion.validar(datos));
    }

    @Test
    void validar_costoNegativo_lanzaExcepcion() {
        Map<String, Object> datos = datosValidos();
        datos.put("costo", -10.0);
        assertThrows(IllegalArgumentException.class, () -> validacion.validar(datos));
    }

    @Test
    void validar_fechaInicioDepuesDeFechaFin_lanzaExcepcion() {
        Map<String, Object> datos = datosValidos();
        datos.put("fechaInicio", LocalDateTime.now().plusDays(10));
        datos.put("fechaFin", LocalDateTime.now().plusDays(1));
        assertThrows(IllegalArgumentException.class, () -> validacion.validar(datos));
    }
}
