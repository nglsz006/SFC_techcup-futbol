package edu.dosw.project.SFC_TechUp_Futbol.core.model;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Pago;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.Partido;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.Torneo;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.state.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ModelStateTest {

    // ── PagoState ──────────────────────────────────────────────────────────────

    @Test
    void pendiente_avanzar_pasaAEnRevision() {
        Pago pago = new Pago();
        pago.avanzar();
        assertEquals(Pago.PagoEstado.EN_REVISION, pago.getEstado());
        assertEquals("EN_REVISION", pago.getState().getNombre());
    }

    @Test
    void pendiente_rechazar_lanzaExcepcion() {
        Pago pago = new Pago();
        assertThrows(IllegalStateException.class, pago::rechazar);
    }

    @Test
    void enRevision_avanzar_pasaAAprobado() {
        Pago pago = new Pago();
        pago.avanzar();
        pago.avanzar();
        assertEquals(Pago.PagoEstado.APROBADO, pago.getEstado());
        assertEquals("APROBADO", pago.getState().getNombre());
    }

    @Test
    void enRevision_rechazar_pasaARechazado() {
        Pago pago = new Pago();
        pago.avanzar();
        pago.rechazar();
        assertEquals(Pago.PagoEstado.RECHAZADO, pago.getEstado());
        assertEquals("RECHAZADO", pago.getState().getNombre());
    }

    @Test
    void aprobado_avanzar_lanzaExcepcion() {
        Pago pago = new Pago();
        pago.avanzar();
        pago.avanzar();
        assertThrows(IllegalStateException.class, pago::avanzar);
    }

    @Test
    void aprobado_rechazar_lanzaExcepcion() {
        Pago pago = new Pago();
        pago.avanzar();
        pago.avanzar();
        assertThrows(IllegalStateException.class, pago::rechazar);
    }

    @Test
    void rechazado_avanzar_lanzaExcepcion() {
        Pago pago = new Pago();
        pago.avanzar();
        pago.rechazar();
        assertThrows(IllegalStateException.class, pago::avanzar);
    }

    @Test
    void rechazado_rechazar_lanzaExcepcion() {
        Pago pago = new Pago();
        pago.avanzar();
        pago.rechazar();
        assertThrows(IllegalStateException.class, pago::rechazar);
    }

    // ── PartidoState ───────────────────────────────────────────────────────────

    @Test
    void programado_getNombre_correcto() {
        Partido p = new Partido();
        assertEquals("PROGRAMADO", p.getState().getNombre());
    }

    @Test
    void programado_iniciar_pasaAEnCurso() {
        Partido p = new Partido();
        p.iniciar();
        assertEquals(Partido.PartidoEstado.EN_CURSO, p.getEstado());
        assertEquals("EN_CURSO", p.getState().getNombre());
    }

    @Test
    void programado_registrarResultado_lanzaExcepcion() {
        Partido p = new Partido();
        assertThrows(IllegalStateException.class, () -> p.registrarResultado(1, 0));
    }

    @Test
    void programado_finalizar_lanzaExcepcion() {
        Partido p = new Partido();
        assertThrows(IllegalStateException.class, p::finalizar);
    }

    @Test
    void enCurso_iniciar_lanzaExcepcion() {
        Partido p = new Partido();
        p.iniciar();
        assertThrows(IllegalStateException.class, p::iniciar);
    }

    @Test
    void enCurso_registrarResultado_actualizaMarcador() {
        Partido p = new Partido();
        p.iniciar();
        p.registrarResultado(2, 1);
        assertEquals(2, p.getMarcadorLocal());
        assertEquals(1, p.getMarcadorVisitante());
    }

    @Test
    void enCurso_finalizar_pasaAFinalizado() {
        Partido p = new Partido();
        p.iniciar();
        p.finalizar();
        assertEquals(Partido.PartidoEstado.FINALIZADO, p.getEstado());
        assertEquals("FINALIZADO", p.getState().getNombre());
    }

    @Test
    void finalizado_iniciar_lanzaExcepcion() {
        Partido p = new Partido();
        p.iniciar();
        p.finalizar();
        assertThrows(IllegalStateException.class, p::iniciar);
    }

    @Test
    void finalizado_registrarResultado_lanzaExcepcion() {
        Partido p = new Partido();
        p.iniciar();
        p.finalizar();
        assertThrows(IllegalStateException.class, () -> p.registrarResultado(1, 1));
    }

    @Test
    void finalizado_finalizar_lanzaExcepcion() {
        Partido p = new Partido();
        p.iniciar();
        p.finalizar();
        assertThrows(IllegalStateException.class, p::finalizar);
    }

    // ── TorneoState ────────────────────────────────────────────────────────────

    @Test
    void torneoCreado_iniciar_pasaAEnCurso() {
        Torneo torneo = new Torneo("uuid-1", "Copa", LocalDateTime.now(), LocalDateTime.now().plusDays(5), 8, 50);
        EstadoTorneoInterface estado = new TorneoCreado();
        EstadoTorneoInterface siguiente = estado.iniciar(torneo);
        assertEquals(Torneo.EstadoTorneo.EN_CURSO, torneo.getEstado());
        assertInstanceOf(TorneoEnCurso.class, siguiente);
    }

    @Test
    void torneoCreado_finalizar_retornaMismo() {
        Torneo torneo = new Torneo("uuid-1", "Copa", LocalDateTime.now(), LocalDateTime.now().plusDays(5), 8, 50);
        EstadoTorneoInterface estado = new TorneoCreado();
        EstadoTorneoInterface mismo = estado.finalizar(torneo);
        assertInstanceOf(TorneoCreado.class, mismo);
    }

    @Test
    void torneoCreado_puedeInscribir_esTrue() {
        assertTrue(new TorneoCreado().puedeInscribirEquipos());
    }

    @Test
    void torneoEnCurso_iniciar_retornaMismo() {
        Torneo torneo = new Torneo("uuid-1", "Copa", LocalDateTime.now(), LocalDateTime.now().plusDays(5), 8, 50);
        EstadoTorneoInterface estado = new TorneoEnCurso();
        assertInstanceOf(TorneoEnCurso.class, estado.iniciar(torneo));
    }

    @Test
    void torneoEnCurso_finalizar_pasaAFinalizado() {
        Torneo torneo = new Torneo("uuid-1", "Copa", LocalDateTime.now(), LocalDateTime.now().plusDays(5), 8, 50);
        EstadoTorneoInterface estado = new TorneoEnCurso();
        EstadoTorneoInterface siguiente = estado.finalizar(torneo);
        assertEquals(Torneo.EstadoTorneo.FINALIZADO, torneo.getEstado());
        assertInstanceOf(TorneoFinalizado.class, siguiente);
    }

    @Test
    void torneoEnCurso_puedeInscribir_esFalse() {
        assertFalse(new TorneoEnCurso().puedeInscribirEquipos());
    }

    @Test
    void torneoFinalizado_iniciar_lanzaExcepcion() {
        Torneo torneo = new Torneo("uuid-1", "Copa", LocalDateTime.now(), LocalDateTime.now().plusDays(5), 8, 50);
        EstadoTorneoInterface estado = new TorneoFinalizado();
        assertThrows(IllegalStateException.class, () -> estado.iniciar(torneo));
    }

    @Test
    void torneoFinalizado_finalizar_lanzaExcepcion() {
        Torneo torneo = new Torneo("uuid-1", "Copa", LocalDateTime.now(), LocalDateTime.now().plusDays(5), 8, 50);
        EstadoTorneoInterface estado = new TorneoFinalizado();
        assertThrows(IllegalStateException.class, () -> estado.finalizar(torneo));
    }

    @Test
    void torneoFinalizado_puedeInscribir_esFalse() {
        assertFalse(new TorneoFinalizado().puedeInscribirEquipos());
    }
}
