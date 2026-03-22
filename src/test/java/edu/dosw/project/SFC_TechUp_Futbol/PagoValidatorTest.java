package edu.dosw.project.SFC_TechUp_Futbol;

import edu.dosw.project.SFC_TechUp_Futbol.core.validator.PagoValidator;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PagoValidatorTest {

    private final PagoValidator validator = new PagoValidator();

    @Test
    void validar_datosCorrectos_noLanzaExcepcion() {
        assertDoesNotThrow(() -> validator.validarSubirComprobante(1L, "comprobante.jpg"));
    }

    @Test
    void validar_equipoIdNulo_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> validator.validarSubirComprobante(null, "comprobante.jpg"));
    }

    @Test
    void validar_comprobanteVacio_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> validator.validarSubirComprobante(1L, ""));
    }

    @Test
    void validar_comprobanteMuyLargo_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> validator.validarSubirComprobante(1L, "x".repeat(501)));
    }
}
