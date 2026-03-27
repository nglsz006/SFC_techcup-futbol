package edu.dosw.project.SFC_TechUp_Futbol;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Jugador;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.Sancion;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.Usuario;
import edu.dosw.project.SFC_TechUp_Futbol.core.validator.PartidoValidator;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class PartidoValidatorTest {

    private final PartidoValidator validator = new PartidoValidator();

    @Test
    void validarCrear_datosCorrectos_noLanzaExcepcion() {
        assertDoesNotThrow(() -> validator.validarCrearPartido(1L, 1L, 2L, LocalDateTime.now(), "cancha 1"));
    }

    @Test
    void validarCrear_torneoIdNulo_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> validator.validarCrearPartido(null, 1L, 2L, LocalDateTime.now(), "cancha"));
    }

    @Test
    void validarCrear_equipoLocalNulo_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> validator.validarCrearPartido(1L, null, 2L, LocalDateTime.now(), "cancha"));
    }

    @Test
    void validarCrear_equipoVisitanteNulo_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> validator.validarCrearPartido(1L, 1L, null, LocalDateTime.now(), "cancha"));
    }

    @Test
    void validarCrear_mismoEquipo_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> validator.validarCrearPartido(1L, 1L, 1L, LocalDateTime.now(), "cancha"));
    }

    @Test
    void validarCrear_fechaNula_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> validator.validarCrearPartido(1L, 1L, 2L, null, "cancha"));
    }

    @Test
    void validarCrear_canchaVacia_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> validator.validarCrearPartido(1L, 1L, 2L, LocalDateTime.now(), ""));
    }

    @Test
    void validarResultado_valido_noLanzaExcepcion() {
        assertDoesNotThrow(() -> validator.validarResultado(2, 1));
    }

    @Test
    void validarResultado_negativo_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> validator.validarResultado(-1, 0));
    }

    @Test
    void validarGoleador_valido_noLanzaExcepcion() {
        assertDoesNotThrow(() -> validator.validarGoleador(1L, 45));
    }

    @Test
    void validarGoleador_idNulo_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> validator.validarGoleador(null, 45));
    }

    @Test
    void validarGoleador_minutoFueraDeRango_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> validator.validarGoleador(1L, 0));
        assertThrows(IllegalArgumentException.class, () -> validator.validarGoleador(1L, 121));
    }

    @Test
    void validarSancion_valida_noLanzaExcepcion() {
        Jugador jugador = new Jugador(1L, "Juan", "juan@test.com", "pass", Usuario.TipoUsuario.ESTUDIANTE, 10, Jugador.Posicion.DELANTERO, true, "");
        Sancion sancion = new Sancion(1L, Sancion.TipoSancion.TARJETA_AMARILLA, "Falta reiterada", jugador);
        assertDoesNotThrow(() -> validator.validarSancion(sancion));
    }

    @Test
    void validarSancion_nula_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> validator.validarSancion(null));
    }

    @Test
    void validarSancion_jugadorNulo_lanzaExcepcion() {
        Sancion sancion = new Sancion(1L, Sancion.TipoSancion.TARJETA_ROJA, "Agresión", null);
        assertThrows(IllegalArgumentException.class, () -> validator.validarSancion(sancion));
    }

    @Test
    void validarSancion_tipoNulo_lanzaExcepcion() {
        Jugador jugador = new Jugador(1L, "Juan", "juan@test.com", "pass", Usuario.TipoUsuario.ESTUDIANTE, 10, Jugador.Posicion.DELANTERO, true, "");
        Sancion sancion = new Sancion(1L, null, "Falta", jugador);
        assertThrows(IllegalArgumentException.class, () -> validator.validarSancion(sancion));
    }

    @Test
    void validarSancion_descripcionVacia_lanzaExcepcion() {
        Jugador jugador = new Jugador(1L, "Juan", "juan@test.com", "pass", Usuario.TipoUsuario.ESTUDIANTE, 10, Jugador.Posicion.DELANTERO, true, "");
        Sancion sancion = new Sancion(1L, Sancion.TipoSancion.AGRESION_VERBAL, "", jugador);
        assertThrows(IllegalArgumentException.class, () -> validator.validarSancion(sancion));
    }

    @Test
    void validarSancion_descripcionNula_lanzaExcepcion() {
        Jugador jugador = new Jugador(1L, "Juan", "juan@test.com", "pass", Usuario.TipoUsuario.ESTUDIANTE, 10, Jugador.Posicion.DELANTERO, true, "");
        Sancion sancion = new Sancion(1L, Sancion.TipoSancion.AGRESION_FISICA, null, jugador);
        assertThrows(IllegalArgumentException.class, () -> validator.validarSancion(sancion));
    }
}