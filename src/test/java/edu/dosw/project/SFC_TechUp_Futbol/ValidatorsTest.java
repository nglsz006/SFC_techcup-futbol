package edu.dosw.project.SFC_TechUp_Futbol;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Partido;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Validators - pruebas unitarias")
class ValidatorsTest {

    @Nested
    @DisplayName("PagoValidator")
    class PagoValidatorTest {

        private PagoValidator validator;

        @BeforeEach
        void setUp() {
            validator = new PagoValidator();
        }

        @Test
        @DisplayName("validarSubirComprobante - no lanza excepcion con datos validos")
        void validar_datosValidos_noLanzaExcepcion() {
            assertThatNoException()
                    .isThrownBy(() -> validator.validarSubirComprobante(1L, "http://comprobante.jpg"));
        }

        @Test
        @DisplayName("validarSubirComprobante - lanza excepcion cuando equipoId es null")
        void validar_equipoIdNull_lanzaIllegalArgumentException() {
            assertThatThrownBy(() -> validator.validarSubirComprobante(null, "http://comprobante.jpg"))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("obligatorio");
        }

        @Test
        @DisplayName("validarSubirComprobante - lanza excepcion cuando comprobante es null")
        void validar_comprobanteNull_lanzaIllegalArgumentException() {
            assertThatThrownBy(() -> validator.validarSubirComprobante(1L, null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("vacío");
        }

        @Test
        @DisplayName("validarSubirComprobante - lanza excepcion cuando comprobante esta en blanco")
        void validar_comprobanteBlanco_lanzaIllegalArgumentException() {
            assertThatThrownBy(() -> validator.validarSubirComprobante(1L, "   "))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("vacío");
        }

        @Test
        @DisplayName("validarSubirComprobante - lanza excepcion cuando comprobante supera 500 caracteres")
        void validar_comprobanteMuyLargo_lanzaIllegalArgumentException() {
            String urlLarga = "http://example.com/" + "a".repeat(490);
            assertThatThrownBy(() -> validator.validarSubirComprobante(1L, urlLarga))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("larga");
        }

        @Test
        @DisplayName("validarSubirComprobante - acepta comprobante en exactamente 500 caracteres")
        void validar_comprobanteEnLimitePreciso_noLanzaExcepcion() {
            String urlExacta = "http://x.co/" + "a".repeat(488); // total = 500
            assertThatNoException()
                    .isThrownBy(() -> validator.validarSubirComprobante(1L, urlExacta));
        }
    }

    @Nested
    @DisplayName("PartidoValidator")
    class PartidoValidatorTest {

        private PartidoValidator validator;

        @BeforeEach
        void setUp() {
            validator = new PartidoValidator();
        }

        @Test
        @DisplayName("validarCrearPartido - no lanza excepcion con datos validos")
        void crearPartido_datosValidos_noLanzaExcepcion() {
            assertThatNoException().isThrownBy(() ->
                    validator.validarCrearPartido(1L, 1L, 2L, LocalDateTime.now(), "Cancha Norte"));
        }

        @Test
        @DisplayName("validarCrearPartido - lanza excepcion cuando torneoId es null")
        void crearPartido_torneoIdNull_lanzaExcepcion() {
            assertThatThrownBy(() ->
                    validator.validarCrearPartido(null, 1L, 2L, LocalDateTime.now(), "Cancha Norte"))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("torneo");
        }

        @Test
        @DisplayName("validarCrearPartido - lanza excepcion cuando equipoLocalId es null")
        void crearPartido_equipoLocalNull_lanzaExcepcion() {
            assertThatThrownBy(() ->
                    validator.validarCrearPartido(1L, null, 2L, LocalDateTime.now(), "Cancha Norte"))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("local");
        }

        @Test
        @DisplayName("validarCrearPartido - lanza excepcion cuando los dos equipos son el mismo")
        void crearPartido_mismoEquipo_lanzaExcepcion() {
            assertThatThrownBy(() ->
                    validator.validarCrearPartido(1L, 1L, 1L, LocalDateTime.now(), "Cancha Norte"))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("mismo");
        }

        @Test
        @DisplayName("validarCrearPartido - lanza excepcion cuando fecha es null")
        void crearPartido_fechaNull_lanzaExcepcion() {
            assertThatThrownBy(() ->
                    validator.validarCrearPartido(1L, 1L, 2L, null, "Cancha Norte"))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("fecha");
        }

        @Test
        @DisplayName("validarCrearPartido - lanza excepcion cuando cancha esta en blanco")
        void crearPartido_canchaBlanca_lanzaExcepcion() {
            assertThatThrownBy(() ->
                    validator.validarCrearPartido(1L, 1L, 2L, LocalDateTime.now(), "  "))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("cancha");
        }

        @Test
        @DisplayName("validarResultado - no lanza excepcion con marcador 0-0")
        void resultado_ceroACero_noLanzaExcepcion() {
            assertThatNoException()
                    .isThrownBy(() -> validator.validarResultado(0, 0));
        }

        @Test
        @DisplayName("validarResultado - no lanza excepcion con marcador positivo")
        void resultado_marcadorPositivo_noLanzaExcepcion() {
            assertThatNoException()
                    .isThrownBy(() -> validator.validarResultado(3, 2));
        }

        @Test
        @DisplayName("validarResultado - lanza excepcion cuando goles local son negativos")
        void resultado_golesLocalNegativo_lanzaExcepcion() {
            assertThatThrownBy(() -> validator.validarResultado(-1, 0))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("negativo");
        }

        @Test
        @DisplayName("validarResultado - lanza excepcion cuando goles visitante son negativos")
        void resultado_golesVisitanteNegativo_lanzaExcepcion() {
            assertThatThrownBy(() -> validator.validarResultado(2, -3))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("negativo");
        }

        @Test
        @DisplayName("validarGoleador - no lanza excepcion con jugadorId valido y minuto 1")
        void goleador_minuto1_noLanzaExcepcion() {
            assertThatNoException()
                    .isThrownBy(() -> validator.validarGoleador(5L, 1));
        }

        @Test
        @DisplayName("validarGoleador - no lanza excepcion con minuto 120 (limite maximo)")
        void goleador_minuto120_noLanzaExcepcion() {
            assertThatNoException()
                    .isThrownBy(() -> validator.validarGoleador(5L, 120));
        }

        @Test
        @DisplayName("validarGoleador - lanza excepcion cuando jugadorId es null")
        void goleador_jugadorIdNull_lanzaExcepcion() {
            assertThatThrownBy(() -> validator.validarGoleador(null, 45))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("jugador");
        }

        @Test
        @DisplayName("validarGoleador - lanza excepcion cuando minuto es 0")
        void goleador_minutoCero_lanzaExcepcion() {
            assertThatThrownBy(() -> validator.validarGoleador(5L, 0))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("minuto");
        }

        @Test
        @DisplayName("validarGoleador - lanza excepcion cuando minuto supera 120")
        void goleador_minuto121_lanzaExcepcion() {
            assertThatThrownBy(() -> validator.validarGoleador(5L, 121))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("minuto");
        }

        @Test
        @DisplayName("validarTarjeta - no lanza excepcion con datos validos AMARILLA")
        void tarjeta_amarillaValida_noLanzaExcepcion() {
            assertThatNoException()
                    .isThrownBy(() -> validator.validarTarjeta(5L, Partido.Tarjeta.TipoTarjeta.AMARILLA, 55));
        }

        @Test
        @DisplayName("validarTarjeta - no lanza excepcion con datos validos ROJA")
        void tarjeta_rojaValida_noLanzaExcepcion() {
            assertThatNoException()
                    .isThrownBy(() -> validator.validarTarjeta(5L, Partido.Tarjeta.TipoTarjeta.ROJA, 88));
        }

        @Test
        @DisplayName("validarTarjeta - lanza excepcion cuando jugadorId es null")
        void tarjeta_jugadorIdNull_lanzaExcepcion() {
            assertThatThrownBy(() -> validator.validarTarjeta(null, Partido.Tarjeta.TipoTarjeta.AMARILLA, 30))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("jugador");
        }

        @Test
        @DisplayName("validarTarjeta - lanza excepcion cuando tipo es null")
        void tarjeta_tipoNull_lanzaExcepcion() {
            assertThatThrownBy(() -> validator.validarTarjeta(5L, null, 30))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("tipo");
        }

        @Test
        @DisplayName("validarTarjeta - lanza excepcion cuando minuto es 0")
        void tarjeta_minutoCero_lanzaExcepcion() {
            assertThatThrownBy(() -> validator.validarTarjeta(5L, Partido.Tarjeta.TipoTarjeta.ROJA, 0))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("minuto");
        }

        @Test
        @DisplayName("validarTarjeta - lanza excepcion cuando minuto supera 120")
        void tarjeta_minuto121_lanzaExcepcion() {
            assertThatThrownBy(() -> validator.validarTarjeta(5L, Partido.Tarjeta.TipoTarjeta.AMARILLA, 121))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("minuto");
        }
    }
}
