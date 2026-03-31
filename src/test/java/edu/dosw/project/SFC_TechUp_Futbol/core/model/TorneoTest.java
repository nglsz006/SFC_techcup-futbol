package edu.dosw.project.SFC_TechUp_Futbol.core.model;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Torneo;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class TorneoTest {

    private Torneo crearTorneo() {
        return new Torneo("uuid-torneo-1", "Copa DOSW", LocalDateTime.now(), LocalDateTime.now().plusDays(5), 8, 50.0);
    }

    @Test
    void torneoNuevo_estadoCreado() {
        assertEquals(Torneo.EstadoTorneo.CREADO, crearTorneo().getEstado());
    }

    @Test
    void iniciar_cambiaaEnCurso() {
        Torneo torneo = crearTorneo();
        torneo.iniciar();
        assertEquals(Torneo.EstadoTorneo.EN_CURSO, torneo.getEstado());
    }

    @Test
    void finalizar_despuesDeIniciar_cambiaaFinalizado() {
        Torneo torneo = crearTorneo();
        torneo.iniciar();
        torneo.finalizar();
        assertEquals(Torneo.EstadoTorneo.FINALIZADO, torneo.getEstado());
    }

    @Test
    void finalizar_sinIniciar_noHaceNada() {
        Torneo torneo = crearTorneo();
        torneo.finalizar();
        assertEquals(Torneo.EstadoTorneo.CREADO, torneo.getEstado());
    }

    @Test
    void iniciar_torneoYaIniciado_noLanzaExcepcion() {
        Torneo torneo = crearTorneo();
        torneo.iniciar();
        torneo.iniciar();
        assertEquals(Torneo.EstadoTorneo.EN_CURSO, torneo.getEstado());
    }
}
