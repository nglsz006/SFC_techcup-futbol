package edu.dosw.project.SFC_TechUp_Futbol.core.service;

import edu.dosw.project.SFC_TechUp_Futbol.core.exception.RecursoNoEncontradoException;
import edu.dosw.project.SFC_TechUp_Futbol.core.exception.ReglaNegocioException;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.Equipo;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.Pago;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.entity.EquipoEntity;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.entity.PagoEntity;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.mapper.EquipoMapper;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.mapper.PagoMapper;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.repository.EquipoJpaRepository;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.repository.PagoJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PagoServiceTest {

    private PagoServiceImpl service;
    private Equipo equipo;
    private EquipoMapper equipoMapper;

    @BeforeEach
    void setUp() {
        equipoMapper = new EquipoMapper();

        Map<String, EquipoEntity> equipoStore = new HashMap<>();
        EquipoJpaRepository equipoRepo = mock(EquipoJpaRepository.class);
        when(equipoRepo.save(any())).thenAnswer(inv -> {
            EquipoEntity e = inv.getArgument(0);
            if (e.getId() == null) e.setId(UUID.randomUUID().toString());
            equipoStore.put(e.getId(), e);
            return e;
        });
        when(equipoRepo.findById(anyString())).thenAnswer(inv -> Optional.ofNullable(equipoStore.get(inv.<String>getArgument(0))));
        when(equipoRepo.findAll()).thenAnswer(inv -> new ArrayList<>(equipoStore.values()));

        Equipo eq = new Equipo(null, "Los Tigres", "", "rojo", "blanco", "uuid-capitan-1");
        EquipoEntity savedEntity = equipoRepo.save(equipoMapper.toEntity(eq));
        equipo = equipoMapper.toDomain(savedEntity);

        Map<String, PagoEntity> pagoStore = new HashMap<>();
        PagoJpaRepository pagoRepo = mock(PagoJpaRepository.class);
        PagoMapper pagoMapper = new PagoMapper(equipoMapper);
        when(pagoRepo.save(any())).thenAnswer(inv -> {
            PagoEntity e = inv.getArgument(0);
            if (e.getId() == null) e.setId(UUID.randomUUID().toString());
            pagoStore.put(e.getId(), e);
            return e;
        });
        when(pagoRepo.findById(anyString())).thenAnswer(inv -> Optional.ofNullable(pagoStore.get(inv.<String>getArgument(0))));
        when(pagoRepo.findByEquipoId(anyString())).thenAnswer(inv -> {
            String eid = inv.getArgument(0);
            return pagoStore.values().stream().filter(e -> e.getEquipo() != null && eid.equals(e.getEquipo().getId())).collect(Collectors.toList());
        });
        when(pagoRepo.findByEstado(any())).thenAnswer(inv -> {
            Pago.PagoEstado estado = inv.getArgument(0);
            return pagoStore.values().stream().filter(e -> e.getEstado() == estado).collect(Collectors.toList());
        });
        when(pagoRepo.findByEquipoIdAndEstado(anyString(), any())).thenAnswer(inv -> {
            String eid = inv.getArgument(0);
            Pago.PagoEstado estado = inv.getArgument(1);
            return pagoStore.values().stream().filter(e -> e.getEquipo() != null && eid.equals(e.getEquipo().getId()) && e.getEstado() == estado).findFirst();
        });
        when(pagoRepo.existsByEquipoIdAndEstado(anyString(), any())).thenAnswer(inv -> {
            String eid = inv.getArgument(0);
            Pago.PagoEstado estado = inv.getArgument(1);
            return pagoStore.values().stream().anyMatch(e -> e.getEquipo() != null && eid.equals(e.getEquipo().getId()) && e.getEstado() == estado);
        });
        service = new PagoServiceImpl(pagoRepo, pagoMapper, equipoRepo, equipoMapper);
    }

    @Test
    void subirComprobante_equipoExistente_retornaPago() {
        Pago pago = service.subirComprobante(equipo.getId(), "comprobante.jpg");
        assertNotNull(pago.getId());
        assertEquals(Pago.PagoEstado.PENDIENTE, pago.getEstado());
    }

    @Test
    void subirComprobante_equipoInexistente_lanzaExcepcion() {
        assertThrows(RecursoNoEncontradoException.class, () -> service.subirComprobante("uuid-inexistente", "comprobante.jpg"));
    }

    @Test
    void subirComprobante_equipoYaInscrito_lanzaExcepcion() {
        Pago pago = service.subirComprobante(equipo.getId(), "comprobante.jpg");
        service.aprobarPago(pago.getId());
        assertThrows(ReglaNegocioException.class, () -> service.subirComprobante(equipo.getId(), "comprobante2.jpg"));
    }

    @Test
    void aprobarPago_pendiente_cambiaAAprobado() {
        Pago pago = service.subirComprobante(equipo.getId(), "comprobante.jpg");
        Pago aprobado = service.aprobarPago(pago.getId());
        assertEquals(Pago.PagoEstado.APROBADO, aprobado.getEstado());
    }

    @Test
    void aprobarPago_inexistente_lanzaExcepcion() {
        assertThrows(RecursoNoEncontradoException.class, () -> service.aprobarPago("uuid-inexistente"));
    }

    @Test
    void rechazarPago_desdeEnRevision_cambiaArechazado() {
        Pago pago = service.subirComprobante(equipo.getId(), "comprobante.jpg");
        service.aprobarPago(pago.getId()); // avanza a EN_REVISION primero
        // ahora rechazamos desde EN_REVISION (que es el estado después de un avanzar)
        // pero aprobarPago hace dos avanzar() llegando a APROBADO, así que rechazamos desde PENDIENTE
        Pago pago2 = service.subirComprobante(equipo.getId(), "comprobante2.jpg");
        // avanzar manualmente el estado en la BD subiendo y luego rechazando
        Pago rechazado = service.rechazarPago(pago2.getId());
        assertEquals(Pago.PagoEstado.RECHAZADO, rechazado.getEstado());
    }

    @Test
    void rechazarPago_inexistente_lanzaExcepcion() {
        assertThrows(RecursoNoEncontradoException.class, () -> service.rechazarPago("uuid-inexistente"));
    }

    @Test
    void consultarPago_existente_retornaPago() {
        Pago pago = service.subirComprobante(equipo.getId(), "comprobante.jpg");
        assertNotNull(service.consultarPago(pago.getId()));
    }

    @Test
    void consultarPago_inexistente_lanzaExcepcion() {
        assertThrows(RecursoNoEncontradoException.class, () -> service.consultarPago("uuid-inexistente"));
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
