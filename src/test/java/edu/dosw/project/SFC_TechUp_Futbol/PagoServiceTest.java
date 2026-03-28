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
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PagoServiceTest {

    private PagoServiceImpl service;
    private Equipo equipo;

    @BeforeEach
    void setUp() {
        Map<Integer, Equipo> equipoStore = new HashMap<>();
        AtomicInteger equipoIdGen = new AtomicInteger(1);
        EquipoRepository equipoRepo = mock(EquipoRepository.class);
        when(equipoRepo.save(any())).thenAnswer(inv -> {
            Equipo e = inv.getArgument(0);
            if (e.getId() == 0) e.setId(equipoIdGen.getAndIncrement());
            equipoStore.put(e.getId(), e);
            return e;
        });
        when(equipoRepo.findById(anyInt())).thenAnswer(inv -> Optional.ofNullable(equipoStore.get(inv.<Integer>getArgument(0))));
        when(equipoRepo.findById(any(Long.class))).thenAnswer(inv -> Optional.ofNullable(equipoStore.get(((Long) inv.getArgument(0)).intValue())));
        when(equipoRepo.findAll()).thenAnswer(inv -> new ArrayList<>(equipoStore.values()));
        equipo = equipoRepo.save(new Equipo(0, "Los Tigres", "", "rojo", "blanco", 1));

        Map<Long, Pago> pagoStore = new HashMap<>();
        AtomicLong pagoIdGen = new AtomicLong(1);
        PagoRepository pagoRepo = mock(PagoRepository.class);
        when(pagoRepo.save(any())).thenAnswer(inv -> {
            Pago p = inv.getArgument(0);
            if (p.getId() == null) p.setId(pagoIdGen.getAndIncrement());
            pagoStore.put(p.getId(), p);
            return p;
        });
        when(pagoRepo.findById(anyLong())).thenAnswer(inv -> Optional.ofNullable(pagoStore.get(inv.<Long>getArgument(0))));
        when(pagoRepo.findByEquipoId(anyLong())).thenAnswer(inv -> {
            Long eid = inv.getArgument(0);
            return pagoStore.values().stream().filter(p -> p.getEquipo() != null && p.getEquipo().getId() == eid.intValue()).collect(Collectors.toList());
        });
        when(pagoRepo.findByEstado(any())).thenAnswer(inv -> {
            Pago.PagoEstado estado = inv.getArgument(0);
            return pagoStore.values().stream().filter(p -> p.getEstado() == estado).collect(Collectors.toList());
        });
        when(pagoRepo.findByEquipoIdAndEstado(anyLong(), any())).thenAnswer(inv -> {
            Long eid = inv.getArgument(0);
            Pago.PagoEstado estado = inv.getArgument(1);
            return pagoStore.values().stream().filter(p -> p.getEquipo() != null && p.getEquipo().getId() == eid.intValue() && p.getEstado() == estado).findFirst();
        });
        when(pagoRepo.existsByEquipoIdAndEstado(anyLong(), any())).thenAnswer(inv -> {
            Long eid = inv.getArgument(0);
            Pago.PagoEstado estado = inv.getArgument(1);
            return pagoStore.values().stream().anyMatch(p -> p.getEquipo() != null && p.getEquipo().getId() == eid.intValue() && p.getEstado() == estado);
        });
        service = new PagoServiceImpl(pagoRepo, equipoRepo);
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
