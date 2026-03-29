package edu.dosw.project.SFC_TechUp_Futbol;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Jugador;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.Sancion;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.Usuario;
import edu.dosw.project.SFC_TechUp_Futbol.core.validator.SancionValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SancionTest {

    private Jugador jugador;
    private SancionValidator sancionValidator;

    @BeforeEach
    void setUp() {
        jugador = new Jugador("uuid-jugador-1", "Juan", "juan@test.com", "pass", Usuario.TipoUsuario.ESTUDIANTE, 10, Jugador.Posicion.DELANTERO, true, "");
        sancionValidator = new SancionValidator();
    }

    @Test
    void crearSancion_datosValidos_creaCorrectamente() {
        Sancion sancion = new Sancion("uuid-1", Sancion.TipoSancion.TARJETA_AMARILLA, "Falta reiterada", jugador);
        assertEquals("uuid-1", sancion.getId());
        assertEquals(Sancion.TipoSancion.TARJETA_AMARILLA, sancion.getTipoSancion());
        assertEquals("Falta reiterada", sancion.getDescripcion());
        assertEquals(jugador, sancion.getJugador());
    }

    @Test
    void crearSancion_constructorVacio_atributosNull() {
        Sancion sancion = new Sancion();
        assertNull(sancion.getId());
        assertNull(sancion.getTipoSancion());
        assertNull(sancion.getDescripcion());
        assertNull(sancion.getJugador());
    }

    @Test
    void crearSancion_todosLosTipos_sonValidos() {
        assertDoesNotThrow(() -> new Sancion("uuid-1", Sancion.TipoSancion.TARJETA_ROJA, "Juego brusco", jugador));
        assertDoesNotThrow(() -> new Sancion("uuid-2", Sancion.TipoSancion.TARJETA_AMARILLA, "Falta táctica", jugador));
        assertDoesNotThrow(() -> new Sancion("uuid-3", Sancion.TipoSancion.AGRESION_VERBAL, "Insultos al árbitro", jugador));
        assertDoesNotThrow(() -> new Sancion("uuid-4", Sancion.TipoSancion.AGRESION_FISICA, "Empujón a rival", jugador));
    }

    @Test
    void enumTipoSancion_contiene4Valores() {
        assertEquals(4, Sancion.TipoSancion.values().length);
    }

    @Test
    void jugadorSinSanciones_retornaListaVacia() {
        assertTrue(jugador.getSanciones().isEmpty());
        assertEquals(0, jugador.getSanciones().size());
    }

    @Test
    void agregarSancion_incrementaListaDelJugador() {
        Sancion sancion = new Sancion("uuid-1", Sancion.TipoSancion.TARJETA_AMARILLA, "Falta en minuto 20", jugador);
        jugador.agregarSancion(sancion);
        assertEquals(1, jugador.getSanciones().size());
    }

    @Test
    void agregarMultiplesSanciones_acumulaCorrectamente() {
        jugador.agregarSancion(new Sancion("uuid-1", Sancion.TipoSancion.TARJETA_AMARILLA, "Primera falta", jugador));
        jugador.agregarSancion(new Sancion("uuid-2", Sancion.TipoSancion.TARJETA_AMARILLA, "Segunda falta", jugador));
        jugador.agregarSancion(new Sancion("uuid-3", Sancion.TipoSancion.TARJETA_ROJA, "Juego brusco", jugador));
        assertEquals(3, jugador.getSanciones().size());
    }

    @Test
    void getSancionesPorTipo_filtraCorrectamente() {
        jugador.agregarSancion(new Sancion("uuid-1", Sancion.TipoSancion.TARJETA_AMARILLA, "Falta 1", jugador));
        jugador.agregarSancion(new Sancion("uuid-2", Sancion.TipoSancion.TARJETA_ROJA, "Juego brusco", jugador));
        jugador.agregarSancion(new Sancion("uuid-3", Sancion.TipoSancion.TARJETA_AMARILLA, "Falta 2", jugador));

        List<Sancion> amarillas = jugador.getSancionesPorTipo(Sancion.TipoSancion.TARJETA_AMARILLA);
        List<Sancion> rojas = jugador.getSancionesPorTipo(Sancion.TipoSancion.TARJETA_ROJA);
        List<Sancion> verbales = jugador.getSancionesPorTipo(Sancion.TipoSancion.AGRESION_VERBAL);

        assertEquals(2, amarillas.size());
        assertEquals(1, rojas.size());
        assertEquals(0, verbales.size());
    }

    @Test
    void tieneSancion_retornaTrueOFalse() {
        jugador.agregarSancion(new Sancion("uuid-1", Sancion.TipoSancion.TARJETA_ROJA, "Expulsado", jugador));

        assertTrue(jugador.tieneSancion(Sancion.TipoSancion.TARJETA_ROJA));
        assertFalse(jugador.tieneSancion(Sancion.TipoSancion.TARJETA_AMARILLA));
        assertFalse(jugador.tieneSancion(Sancion.TipoSancion.AGRESION_VERBAL));
        assertFalse(jugador.tieneSancion(Sancion.TipoSancion.AGRESION_FISICA));
    }

    @Test
    void multiplesSancionesDelMismoTipo_seAcumulan() {
        jugador.agregarSancion(new Sancion("uuid-1", Sancion.TipoSancion.AGRESION_VERBAL, "Insulto 1", jugador));
        jugador.agregarSancion(new Sancion("uuid-2", Sancion.TipoSancion.AGRESION_VERBAL, "Insulto 2", jugador));
        assertEquals(2, jugador.getSancionesPorTipo(Sancion.TipoSancion.AGRESION_VERBAL).size());
    }

    @Test
    void validarSancion_valida_noLanzaExcepcion() {
        Sancion sancion = new Sancion("uuid-1", Sancion.TipoSancion.TARJETA_AMARILLA, "Falta", jugador);
        assertDoesNotThrow(() -> sancionValidator.validarSancion(sancion));
    }

    @Test
    void validarSancion_nula_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> sancionValidator.validarSancion(null));
    }

    @Test
    void validarSancion_tipoNulo_lanzaExcepcion() {
        Sancion sancion = new Sancion("uuid-1", null, "Falta", jugador);
        assertThrows(IllegalArgumentException.class, () -> sancionValidator.validarSancion(sancion));
    }

    @Test
    void validarSancion_descripcionVacia_lanzaExcepcion() {
        Sancion sancion = new Sancion("uuid-1", Sancion.TipoSancion.TARJETA_ROJA, "", jugador);
        assertThrows(IllegalArgumentException.class, () -> sancionValidator.validarSancion(sancion));
    }

    @Test
    void validarSancion_descripcionNula_lanzaExcepcion() {
        Sancion sancion = new Sancion("uuid-1", Sancion.TipoSancion.AGRESION_FISICA, null, jugador);
        assertThrows(IllegalArgumentException.class, () -> sancionValidator.validarSancion(sancion));
    }

    @Test
    void esValida_sancionCompleta_retornaTrue() {
        Sancion sancion = new Sancion("uuid-1", Sancion.TipoSancion.TARJETA_AMARILLA, "Falta", jugador);
        assertTrue(sancionValidator.esValida(sancion));
    }

    @Test
    void esValida_sancionIncompleta_retornaFalse() {
        assertFalse(sancionValidator.esValida(null));
        assertFalse(sancionValidator.esValida(new Sancion("uuid-1", null, "Falta", jugador)));
        assertFalse(sancionValidator.esValida(new Sancion("uuid-1", Sancion.TipoSancion.TARJETA_ROJA, "", jugador)));
        assertFalse(sancionValidator.esValida(new Sancion("uuid-1", Sancion.TipoSancion.TARJETA_ROJA, null, jugador)));
    }
}
