package edu.dosw.project.SFC_TechUp_Futbol;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Jugador;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.Posicion;
import edu.dosw.project.SFC_TechUp_Futbol.core.validator.JugadorValidator;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class JugadorValidatorTest {

    private final JugadorValidator validator = new JugadorValidator();

    @Test
    void dorsalValido_mayorACero() {
        assertTrue(validator.dorsalValido(10));
    }

    @Test
    void dorsalInvalido_cero() {
        assertFalse(validator.dorsalValido(0));
    }

    @Test
    void dorsalInvalido_negativo() {
        assertFalse(validator.dorsalValido(-1));
    }

    @Test
    void posicionValida_noNula() {
        assertTrue(validator.posicionValida(Posicion.PORTERO));
    }

    @Test
    void posicionInvalida_nula() {
        assertFalse(validator.posicionValida(null));
    }

    @Test
    void jugadorDisponible_cuandoEstaDisponible() {
        Jugador jugador = new Jugador();
        jugador.setAvailable(true);
        assertTrue(validator.jugadorDisponibleParaEquipo(jugador));
    }

    @Test
    void jugadorNoDisponible_cuandoYaTieneEquipo() {
        Jugador jugador = new Jugador();
        jugador.setAvailable(false);
        assertFalse(validator.jugadorDisponibleParaEquipo(jugador));
    }
}
