package edu.dosw.project.SFC_TechUp_Futbol;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Equipo;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.Pago;
import edu.dosw.project.SFC_TechUp_Futbol.core.repository.EquipoRepository;
import edu.dosw.project.SFC_TechUp_Futbol.core.repository.PagoRepository;
import edu.dosw.project.SFC_TechUp_Futbol.core.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class PagoServiceImplTest {

    @Mock
    private PagoRepository pagoRepository;

    @Mock
    private EquipoRepository equipoRepository;

    @InjectMocks
    private PagoServiceImpl pagoService;

    private Equipo equipoMock;

    @BeforeEach
    void setUp() {
        equipoMock = new Equipo();
        equipoMock.setId(1);
        equipoMock.setNombre("Equipo A");
    }

    @Test
        @DisplayName("subirComprobante - crea pago correctamente cuando el equipo existe y no tiene pago aprobado")
        void subirComprobante_equipoExisteSinPagoAprobado_creaYGuardaPago() {
            when(equipoRepository.findById(1)).thenReturn(Optional.of(equipoMock));
            when(pagoRepository.existsByEquipoIdAndEstado(1L, Pago.PagoEstado.APROBADO)).thenReturn(false);
            when(pagoRepository.save(any(Pago.class))).thenAnswer(inv -> {
                Pago p = inv.getArgument(0);
                p.setId(10L);
                return p;
            });

            Pago resultado = pagoService.subirComprobante(1L, "http://comprobante.jpg");

            assertThat(resultado).isNotNull();
            assertThat(resultado.getComprobante()).isEqualTo("http://comprobante.jpg");
            assertThat(resultado.getEquipo()).isEqualTo(equipoMock);
            assertThat(resultado.getEstado()).isEqualTo(Pago.PagoEstado.PENDIENTE);
            verify(pagoRepository).save(any(Pago.class));
        }

    @Test
    @DisplayName("subirComprobante - lanza excepcion cuando el equipo no existe")
    void subirComprobante_equipoNoExiste_lanzaRuntimeException() {
        when(equipoRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> pagoService.subirComprobante(99L, "http://comprobante.jpg"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Equipo no encontrado");

        verify(pagoRepository, never()).save(any());
    }

    @Test
    @DisplayName("subirComprobante - lanza excepcion cuando el equipo ya tiene un pago aprobado")
    void subirComprobante_equipoConPagoAprobado_lanzaIllegalStateException() {
        when(equipoRepository.findById(1)).thenReturn(Optional.of(equipoMock));
        when(pagoRepository.existsByEquipoIdAndEstado(1L, Pago.PagoEstado.APROBADO)).thenReturn(true);

        assertThatThrownBy(() -> pagoService.subirComprobante(1L, "http://comprobante.jpg"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("pago aprobado");

        verify(pagoRepository, never()).save(any());
    }

    @Test
        @DisplayName("aprobarPago - aprueba un pago en estado PENDIENTE correctamente")
        void aprobarPago_estadoPendiente_avanzaAEnRevisionYGuarda() {
            Pago pago = new Pago(); // estado inicial: PENDIENTE
            pago.setId(5L);
            pago.setEquipo(equipoMock);
            when(pagoRepository.findById(5L)).thenReturn(Optional.of(pago));
            when(pagoRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

            Pago resultado = pagoService.aprobarPago(5L);

            assertThat(resultado.getEstado()).isEqualTo(Pago.PagoEstado.EN_REVISION);
                    verify(pagoRepository).save(pago);
            }

    @Test
    @DisplayName("aprobarPago - lanza excepcion cuando el pago no existe")
    void aprobarPago_pagoNoExiste_lanzaRuntimeException() {
        when(pagoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> pagoService.aprobarPago(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Pago no encontrado");
    }

    @Test
    @DisplayName("aprobarPago - no llama avanzar si el pago no esta en PENDIENTE")
    void aprobarPago_estadoNoEsPendiente_noLlamaAvanzar() {
        Pago pago = new Pago();
        pago.setId(5L);
        pago.avanzar(); // PENDIENTE -> EN_REVISION
        when(pagoRepository.findById(5L)).thenReturn(Optional.of(pago));
        when(pagoRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Pago resultado = pagoService.aprobarPago(5L);

        // ya estaba en EN_REVISION, no debe cambiar
        assertThat(resultado.getEstado()).isEqualTo(Pago.PagoEstado.EN_REVISION);
    }

    @Test
    @DisplayName("rechazarPago - rechaza un pago en estado EN_REVISION correctamente")
    void rechazarPago_estadoEnRevision_cambiaARechadoYGuarda() {
        Pago pago = new Pago();
        pago.setId(6L);
        pago.avanzar(); // PENDIENTE -> EN_REVISION
        when(pagoRepository.findById(6L)).thenReturn(Optional.of(pago));
        when(pagoRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Pago resultado = pagoService.rechazarPago(6L);

        assertThat(resultado.getEstado()).isEqualTo(Pago.PagoEstado.RECHAZADO);
        verify(pagoRepository).save(pago);
    }

    @Test
    @DisplayName("rechazarPago - lanza excepcion al rechazar un pago en PENDIENTE (State no lo permite)")
    void rechazarPago_estadoPendiente_lanzaIllegalStateException() {
        Pago pago = new Pago(); // PENDIENTE
        pago.setId(7L);
        when(pagoRepository.findById(7L)).thenReturn(Optional.of(pago));

        assertThatThrownBy(() -> pagoService.rechazarPago(7L))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("rechazarPago - lanza excepcion al rechazar un pago ya APROBADO")
    void rechazarPago_estadoAprobado_lanzaIllegalStateException() {
        Pago pago = new Pago();
        pago.avanzar(); // -> EN_REVISION
        pago.avanzar(); // -> APROBADO
        pago.setId(8L);
        when(pagoRepository.findById(8L)).thenReturn(Optional.of(pago));

        assertThatThrownBy(() -> pagoService.rechazarPago(8L))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("consultarPago - retorna el pago cuando existe")
    void consultarPago_pagoExiste_retornaPago() {
        Pago pago = new Pago();
        pago.setId(3L);
        when(pagoRepository.findById(3L)).thenReturn(Optional.of(pago));

        Pago resultado = pagoService.consultarPago(3L);

        assertThat(resultado.getId()).isEqualTo(3L);
    }

    @Test
    @DisplayName("consultarPago - lanza excepcion cuando el pago no existe")
    void consultarPago_pagoNoExiste_lanzaRuntimeException() {
        when(pagoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> pagoService.consultarPago(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Pago no encontrado");
    }

    @Test
    @DisplayName("consultarPagosPorEquipo - retorna lista de pagos del equipo")
    void consultarPagosPorEquipo_retornaListaCorrectamente() {
        Pago p1 = new Pago(); p1.setId(1L);
        Pago p2 = new Pago(); p2.setId(2L);
        when(pagoRepository.findByEquipoId(1L)).thenReturn(List.of(p1, p2));

        List<Pago> resultado = pagoService.consultarPagosPorEquipo(1L);

        assertThat(resultado).hasSize(2);
    }

    @Test
    @DisplayName("consultarPagosPendientes - retorna solo pagos en estado PENDIENTE")
    void consultarPagosPendientes_retornaListaDePendientes() {
        Pago p1 = new Pago(); p1.setId(1L);
        when(pagoRepository.findByEstado(Pago.PagoEstado.PENDIENTE)).thenReturn(List.of(p1));

        List<Pago> resultado = pagoService.consultarPagosPendientes();

        assertThat(resultado).hasSize(1);
        verify(pagoRepository).findByEstado(Pago.PagoEstado.PENDIENTE);
    }
}



