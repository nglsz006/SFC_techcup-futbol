package edu.dosw.project.SFC_TechUp_Futbol;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Equipo;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.Pago;
import edu.dosw.project.SFC_TechUp_Futbol.core.service.PagoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PagoServiceTest {

    private PagoServiceImpl service;
    private EquipoRepositoryImpl equipoRepo;
    private Equipo equipo;

    @BeforeEach
    void setUp() {
        equipoRepo = new EquipoRepositoryImpl();
        service = new PagoServiceImpl(new PagoRepositoryImpl(), equipoRepo);
        equipo = equipoRepo.save(new Equipo(0, "Los Tigres", "", "rojo", "blanco", 1));
    }

    @Test
    void subirComprobante_equipoExistente_retornaPago() {
        Pago pago = service.subirComprobante((long) equipo.getId(), "comprobante.jpg");
        assertNotNull(pago.getId());
        assertEquals(Pago.PagoEstado.PENDIENTE, pago.getEstado());
    }

    @Test
    void subirComprobante_equipoInexistente_lanzaExcepcion() {
        assertThrows(RuntimeException.class, () -> service.subirComprobante(99L, "comprobante.jpg"));
    }

    @Test
    void aprobarPago_pendiente_cambaEstado() {
        Pago pago = service.subirComprobante((long) equipo.getId(), "comprobante.jpg");
        Pago aprobado = service.aprobarPago(pago.getId());
        assertNotEquals(Pago.PagoEstado.PENDIENTE, aprobado.getEstado());
    }

    @Test
    void rechazarPago_pendiente_cambaArechazado() {
        Pago pago = service.subirComprobante((long) equipo.getId(), "comprobante.jpg");
        service.aprobarPago(pago.getId());
        Pago rechazado = service.rechazarPago(pago.getId());
        assertEquals(Pago.PagoEstado.RECHAZADO, rechazado.getEstado());
    }

    @Test
    void consultarPago_existente_retornaPago() {
        Pago pago = service.subirComprobante((long) equipo.getId(), "comprobante.jpg");
        assertNotNull(service.consultarPago(pago.getId()));
    }

    @Test
    void consultarPago_inexistente_lanzaExcepcion() {
        assertThrows(RuntimeException.class, () -> service.consultarPago(99L));
    }

    @Test
    void consultarPagosPorEquipo_retornaLista() {
        service.subirComprobante((long) equipo.getId(), "comprobante.jpg");
        assertEquals(1, service.consultarPagosPorEquipo((long) equipo.getId()).size());
    }

    @Test
    void consultarPagosPendientes_retornaLista() {
        service.subirComprobante((long) equipo.getId(), "comprobante.jpg");
        assertFalse(service.consultarPagosPendientes().isEmpty());
    }
}
