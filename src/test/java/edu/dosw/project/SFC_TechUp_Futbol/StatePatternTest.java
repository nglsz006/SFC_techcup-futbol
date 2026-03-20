package edu.dosw.project.SFC_TechUp_Futbol;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Pago;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.Partido;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.state.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Patrón State - pruebas unitarias")
class StatePatternTest {


    @Nested
    @DisplayName("PagoState — flujo completo de estados")
    class PagoStateTest {

        @Test
        @DisplayName("Pago nuevo inicia en estado PENDIENTE con PendienteState")
        void nuevoPago_estadoInicialEsPendiente() {
            Pago pago = new Pago();
            assertThat(pago.getEstado()).isEqualTo(Pago.PagoEstado.PENDIENTE);
            assertThat(pago.getState()).isInstanceOf(PendienteState.class);
        }

        @Test
        @DisplayName("PENDIENTE -> avanzar() -> EN_REVISION")
        void pendiente_avanzar_cambiaAEnRevision() {
            Pago pago = new Pago();
            pago.avanzar();
            assertThat(pago.getEstado()).isEqualTo(Pago.PagoEstado.EN_REVISION);
            assertThat(pago.getState()).isInstanceOf(EnRevisionState.class);
        }

        @Test
        @DisplayName("EN_REVISION -> avanzar() -> APROBADO")
        void enRevision_avanzar_cambiaAAprobado() {
            Pago pago = new Pago();
            pago.avanzar();
            pago.avanzar();
            assertThat(pago.getEstado()).isEqualTo(Pago.PagoEstado.APROBADO);
            assertThat(pago.getState()).isInstanceOf(AprobadoState.class);
        }

        @Test
        @DisplayName("EN_REVISION -> rechazar() -> RECHAZADO")
        void enRevision_rechazar_cambiaARechadado() {
            Pago pago = new Pago();
            pago.avanzar();
            pago.rechazar();
            assertThat(pago.getEstado()).isEqualTo(Pago.PagoEstado.RECHAZADO);
            assertThat(pago.getState()).isInstanceOf(RechazadoState.class);
        }

        @Test
        @DisplayName("PENDIENTE -> rechazar() lanza IllegalStateException")
        void pendiente_rechazar_lanzaExcepcion() {
            Pago pago = new Pago();
            assertThatThrownBy(pago::rechazar)
                    .isInstanceOf(IllegalStateException.class);
        }

        @Test
        @DisplayName("APROBADO -> avanzar() lanza IllegalStateException (estado terminal)")
        void aprobado_avanzar_lanzaExcepcion() {
            Pago pago = new Pago();
            pago.avanzar();
            pago.avanzar();
            assertThatThrownBy(pago::avanzar)
                    .isInstanceOf(IllegalStateException.class);
        }

        @Test
        @DisplayName("APROBADO -> rechazar() lanza IllegalStateException (estado terminal)")
        void aprobado_rechazar_lanzaExcepcion() {
            Pago pago = new Pago();
            pago.avanzar();
            pago.avanzar();
            assertThatThrownBy(pago::rechazar)
                    .isInstanceOf(IllegalStateException.class);
        }

        @Test
        @DisplayName("RECHAZADO -> avanzar() lanza IllegalStateException (estado terminal)")
        void rechazado_avanzar_lanzaExcepcion() {
            Pago pago = new Pago();
            pago.avanzar();
            pago.rechazar();
            assertThatThrownBy(pago::avanzar)
                    .isInstanceOf(IllegalStateException.class);
        }

        @Test
        @DisplayName("RECHAZADO -> rechazar() lanza IllegalStateException (estado terminal)")
        void rechazado_rechazar_lanzaExcepcion() {
            Pago pago = new Pago();
            pago.avanzar();
            pago.rechazar();
            assertThatThrownBy(pago::rechazar)
                    .isInstanceOf(IllegalStateException.class);
        }
    }

    @Nested
    @DisplayName("PartidoState — flujo completo de estados")
    class PartidoStateTest {

        @Test
        @DisplayName("Partido nuevo inicia en PROGRAMADO con ProgramadoState")
        void nuevoPartido_estadoInicialEsProgramado() {
            Partido partido = new Partido();
            assertThat(partido.getEstado()).isEqualTo(Partido.PartidoEstado.PROGRAMADO);
            assertThat(partido.getState()).isInstanceOf(ProgramadoState.class);
        }

        @Test
        @DisplayName("PROGRAMADO -> iniciar() -> EN_CURSO")
        void programado_iniciar_cambiaAEnCurso() {
            Partido partido = new Partido();
            partido.iniciar();
            assertThat(partido.getEstado()).isEqualTo(Partido.PartidoEstado.EN_CURSO);
            assertThat(partido.getState()).isInstanceOf(EnCursoState.class);
        }

        @Test
        @DisplayName("EN_CURSO -> finalizar() -> FINALIZADO")
        void enCurso_finalizar_cambiaAFinalizado() {
            Partido partido = new Partido();
            partido.iniciar();
            partido.finalizar();
            assertThat(partido.getEstado()).isEqualTo(Partido.PartidoEstado.FINALIZADO);
            assertThat(partido.getState()).isInstanceOf(FinalizadoPartidoState.class);
        }

        @Test
        @DisplayName("EN_CURSO -> registrarResultado() actualiza marcador sin cambiar estado")
        void enCurso_registrarResultado_actualizaMarcador() {
            Partido partido = new Partido();
            partido.iniciar();
            partido.registrarResultado(2, 1);
            assertThat(partido.getMarcadorLocal()).isEqualTo(2);
            assertThat(partido.getMarcadorVisitante()).isEqualTo(1);
            assertThat(partido.getEstado()).isEqualTo(Partido.PartidoEstado.EN_CURSO);
        }

        @Test
        @DisplayName("PROGRAMADO -> registrarResultado() lanza IllegalStateException")
        void programado_registrarResultado_lanzaExcepcion() {
            Partido partido = new Partido();
            assertThatThrownBy(() -> partido.registrarResultado(1, 0))
                    .isInstanceOf(IllegalStateException.class);
        }

        @Test
        @DisplayName("PROGRAMADO -> finalizar() lanza IllegalStateException")
        void programado_finalizar_lanzaExcepcion() {
            Partido partido = new Partido();
            assertThatThrownBy(partido::finalizar)
                    .isInstanceOf(IllegalStateException.class);
        }

        @Test
        @DisplayName("EN_CURSO -> iniciar() lanza IllegalStateException")
        void enCurso_iniciar_lanzaExcepcion() {
            Partido partido = new Partido();
            partido.iniciar();
            assertThatThrownBy(partido::iniciar)
                    .isInstanceOf(IllegalStateException.class);
        }

        @Test
        @DisplayName("FINALIZADO -> iniciar() lanza IllegalStateException (estado terminal)")
        void finalizado_iniciar_lanzaExcepcion() {
            Partido partido = new Partido();
            partido.iniciar();
            partido.finalizar();
            assertThatThrownBy(partido::iniciar)
                    .isInstanceOf(IllegalStateException.class);
        }

        @Test
        @DisplayName("FINALIZADO -> registrarResultado() lanza IllegalStateException (estado terminal)")
        void finalizado_registrarResultado_lanzaExcepcion() {
            Partido partido = new Partido();
            partido.iniciar();
            partido.finalizar();
            assertThatThrownBy(() -> partido.registrarResultado(3, 1))
                    .isInstanceOf(IllegalStateException.class);
        }

        @Test
        @DisplayName("FINALIZADO -> finalizar() lanza IllegalStateException (estado terminal)")
        void finalizado_finalizar_lanzaExcepcion() {
            Partido partido = new Partido();
            partido.iniciar();
            partido.finalizar();
            assertThatThrownBy(partido::finalizar)
                    .isInstanceOf(IllegalStateException.class);
        }
    }
}
