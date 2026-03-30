package edu.dosw.project.SFC_TechUp_Futbol;

import edu.dosw.project.SFC_TechUp_Futbol.core.validator.PagoValidator;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PagoValidatorTest {

    private final PagoValidator validator = new PagoValidator();

    @Test
    void validar_datosCorrectos_noLanzaExcepcion() {
        assertDoesNotThrow(() -> validator.validarSubirComprobante("uuid-equipo-1", "comprobante.jpg"));
    }

    @Test
    void validar_equipoIdNulo_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> validator.validarSubirComprobante(null, "comprobante.jpg"));
    }

    @Test
    void validar_equipoIdBlank_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> validator.validarSubirComprobante("   ", "comprobante.jpg"));
    }

    @Test
    void validar_comprobanteVacio_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> validator.validarSubirComprobante("uuid-equipo-1", ""));
    }

    @Test
    void validar_comprobanteMuyLargo_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> validator.validarSubirComprobante("uuid-equipo-1", "x".repeat(501)));
    }
}
