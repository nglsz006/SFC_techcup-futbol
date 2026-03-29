package edu.dosw.project.SFC_TechUp_Futbol;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Equipo;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.Pago;
import edu.dosw.project.SFC_TechUp_Futbol.core.repository.EquipoRepository;
import edu.dosw.project.SFC_TechUp_Futbol.core.repository.PagoRepository;
import edu.dosw.project.SFC_TechUp_Futbol.core.service.PagoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PagoServiceTest {

    private PagoServiceImpl service;
    private Equipo equipo;

    @BeforeEach
    void setUp() {
        Map<String, Equipo> equipoStore = new HashMap<>();
        EquipoRepository equipoRepo = mock(EquipoRepository.class);
        when(equipoRepo.save(any())).thenAnswer(inv -> {
            Equipo e = inv.getArgument(0);
            if (e.getId() == null) e.setId(UUID.randomUUID().toString());
            equipoStore.put(e.getId(), e);
            return e;
        });
        when(equipoRepo.findById(anyString())).thenAnswer(inv -> Optional.ofNullable(equipoStore.get(inv.<String>getArgument(0))));
        when(equipoRepo.findAll()).thenAnswer(inv -> new ArrayList<>(equipoStore.values()));
        equipo = equipoRepo.save(new Equipo(null, "Los Tigres", "", "rojo", "blanco", "uuid-capitan-1"));

        Map<String, Pago> pagoStore = new HashMap<>();
        PagoRepository pagoRepo = mock(PagoRepository.class);
        when(pagoRepo.save(any())).thenAnswer(inv -> {
            Pago p = inv.getArgument(0);
            if (p.getId() == null) p.setId(UUID.randomUUID().toString());
            pagoStore.put(p.getId(), p);
            return p;
        });
        when(pagoRepo.findById(anyString())).thenAnswer(inv -> Optional.ofNullable(pagoStore.get(inv.<String>getArgument(0))));
        when(pagoRepo.findByEquipoId(anyString())).thenAnswer(inv -> {
            String eid = inv.getArgument(0);
            return pagoStore.values().stream().filter(p -> p.getEquipo() != null && eid.equals(p.getEquipo().getId())).collect(Collectors.toList());
        });
        when(pagoRepo.findByEstado(any())).thenAnswer(inv -> {
            Pago.PagoEstado estado = inv.getArgument(0);
            return pagoStore.values().stream().filter(p -> p.getEstado() == estado).collect(Collectors.toList());
        });
        when(pagoRepo.findByEquipoIdAndEstado(anyString(), any())).thenAnswer(inv -> {
            String eid = inv.getArgument(0);
            Pago.PagoEstado estado = inv.getArgument(1);
            return pagoStore.values().stream().filter(p -> p.getEquipo() != null && eid.equals(p.getEquipo().getId()) && p.getEstado() == estado).findFirst();
        });
        when(pagoRepo.existsByEquipoIdAndEstado(anyString(), any())).thenAnswer(inv -> {
            String eid = inv.getArgument(0);
            Pago.PagoEstado estado = inv.getArgument(1);
            return pagoStore.values().stream().anyMatch(p -> p.getEquipo() != null && eid.equals(p.getEquipo().getId()) && p.getEstado() == estado);
        });
        service = new PagoServiceImpl(pagoRepo, equipoRepo);
    }

    @Test
    void subirComprobante_equipoExistente_retornaPago() {
        Pago pago = service.subirComprobante(equipo.getId(), "comprobante.jpg");
        assertNotNull(pago.getId());
        assertEquals(Pago.PagoEstado.PENDIENTE, pago.getEstado());
    }

    @Test
    void subirComprobante_equipoInexistente_lanzaExcepcion() {
        assertThrows(RuntimeException.class, () -> service.subirComprobante("uuid-inexistente", "comprobante.jpg"));
    }

    @Test
    void aprobarPago_pendiente_cambaEstado() {
        Pago pago = service.subirComprobante(equipo.getId(), "comprobante.jpg");
        Pago aprobado = service.aprobarPago(pago.getId());
        assertNotEquals(Pago.PagoEstado.PENDIENTE, aprobado.getEstado());
    }

    @Test
    void rechazarPago_pendiente_cambaArechazado() {
        Pago pago = service.subirComprobante(equipo.getId(), "comprobante.jpg");
        service.aprobarPago(pago.getId());
        Pago rechazado = service.rechazarPago(pago.getId());
        assertEquals(Pago.PagoEstado.RECHAZADO, rechazado.getEstado());
    }

    @Test
    void consultarPago_existente_retornaPago() {
        Pago pago = service.subirComprobante(equipo.getId(), "comprobante.jpg");
        assertNotNull(service.consultarPago(pago.getId()));
    }

    @Test
    void consultarPago_inexistente_lanzaExcepcion() {
        assertThrows(RuntimeException.class, () -> service.consultarPago("uuid-inexistente"));
    }

    @Test
    void consultarPagosPorEquipo_retornaLista() {
        service.subirComprobante(equipo.getId(), "comprobante.jpg");
        assertEquals(1, service.consultarPagosPorEquipo(equipo.getId()).size());
    }

    @Test
    void consultarPagosPendientes_retornaLista() {
        service.subirComprobante(equipo.getId(), "comprobante.jpg");
        assertFalse(service.consultarPagosPendientes().isEmpty());
    }
}
