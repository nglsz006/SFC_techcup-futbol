package edu.dosw.project.SFC_TechUp_Futbol.core.validator;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Alineacion;
import edu.dosw.project.SFC_TechUp_Futbol.core.validator.ValidacionAlineacion;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class ValidacionAlineacionTest {

    private final ValidacionAlineacion validacion = new ValidacionAlineacion();

    private Map<String, Object> datosValidos() {
        return Map.of(
                "formacion", "4-4-2",
                "titulares", List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11)
        );
    }

    @Test
    void validar_datosCorrectos_noLanzaExcepcion() {
        assertDoesNotThrow(() -> validacion.validar(datosValidos()));
    }

    @Test
    void validar_formacionInvalida_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> validacion.validar(Map.of(
                "formacion", "1-1-1",
                "titulares", List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11)
        )));
    }

    @Test
    void validar_menosDe11Titulares_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> validacion.validar(Map.of(
                "formacion", "4-3-3",
                "titulares", List.of(1, 2, 3)
        )));
    }

    @Test
    void validar_masde7Reservas_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> validacion.validar(Map.of(
                "formacion", "4-4-2",
                "titulares", List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11),
                "reservas", List.of(12, 13, 14, 15, 16, 17, 18, 19)
        )));
    }

    @Test
    void formacion_fromString_todasLasFormaciones() {
        assertEquals(Alineacion.Formacion.F_4_4_2, Alineacion.Formacion.fromString("4-4-2"));
        assertEquals(Alineacion.Formacion.F_4_3_3, Alineacion.Formacion.fromString("4-3-3"));
        assertEquals(Alineacion.Formacion.F_3_5_2, Alineacion.Formacion.fromString("3-5-2"));
        assertEquals(Alineacion.Formacion.F_4_5_1, Alineacion.Formacion.fromString("4-5-1"));
        assertEquals(Alineacion.Formacion.F_5_3_2, Alineacion.Formacion.fromString("5-3-2"));
        assertNull(Alineacion.Formacion.fromString("invalido"));
    }
}
