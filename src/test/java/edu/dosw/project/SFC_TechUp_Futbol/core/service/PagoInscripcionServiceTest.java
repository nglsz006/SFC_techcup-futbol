package edu.dosw.project.SFC_TechUp_Futbol.core.service;

import edu.dosw.project.SFC_TechUp_Futbol.core.exception.RecursoNoEncontradoException;
import edu.dosw.project.SFC_TechUp_Futbol.core.exception.ReglaNegocioException;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.Equipo;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.Pago;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.entity.EquipoEntity;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.entity.PagoEntity;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.mapper.EquipoMapper;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.mapper.PagoMapper;
import edu.dosw.project.SFC_TechUp_Futbol.TestMappers;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.repository.EquipoJpaRepository;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.repository.PagoJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PagoInscripcionServiceTest {

    private PagoServiceImpl service;
    private Equipo equipo;
    private EquipoMapper equipoMapper;

    @BeforeEach
    void setUp() {
        equipoMapper = TestMappers.equipoMapper();

        Map<String, EquipoEntity> equipoStore = new HashMap<>();
        EquipoJpaRepository equipoRepo = mock(EquipoJpaRepository.class);
        when(equipoRepo.save(any())).thenAnswer(inv -> {
            EquipoEntity e = inv.getArgument(0);
            if (e.getId() == null) e.setId(UUID.randomUUID().toString());
            equipoStore.put(e.getId(), e);
            return e;
        });
        when(equipoRepo.findById(anyString())).thenAnswer(inv -> Optional.ofNullable(equipoStore.get(inv.<String>getArgument(0))));

        Equipo eq = new Equipo(null, "Los Tigres", "", "rojo", "blanco", "cap-1");
        EquipoEntity savedEntity = equipoRepo.save(equipoMapper.toEntity(eq));
        equipo = equipoMapper.toDomain(savedEntity);

        Map<String, PagoEntity> pagoStore = new HashMap<>();
        PagoJpaRepository pagoRepo = mock(PagoJpaRepository.class);
        PagoMapper pagoMapper = TestMappers.pagoMapper(equipoMapper);
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
    void subirComprobante_equipoExistente_retornaPagoEnPendiente() {
        Pago pago = service.subirComprobante(equipo.getId(), "comprobante.jpg");
        assertNotNull(pago.getId());
        assertEquals(Pago.PagoEstado.PENDIENTE, pago.getEstado());
    }

    @Test
    void subirComprobante_equipoInexistente_lanzaExcepcion() {
        assertThrows(RecursoNoEncontradoException.class, () ->
                service.subirComprobante("equipo-inexistente", "comprobante.jpg"));
    }

    @Test
    void subirComprobante_comprobanteVacio_noEsValidadoEnServicio() {
        Pago pago = service.subirComprobante(equipo.getId(), "");
        assertNotNull(pago.getId());
    }

    @Test
    void subirComprobante_equipoYaAprobado_lanzaReglaNegocio() {
        Pago pago = service.subirComprobante(equipo.getId(), "comprobante.jpg");
        service.aprobarPago(pago.getId());
        assertThrows(ReglaNegocioException.class, () ->
                service.subirComprobante(equipo.getId(), "comprobante2.jpg"));
    }

    @Test
    void enviarARevision_desdePendiente_cambiaAEnRevision() {
        Pago pago = service.subirComprobante(equipo.getId(), "comprobante.jpg");
        Pago enRevision = service.enviarARevision(pago.getId());
        assertEquals(Pago.PagoEstado.EN_REVISION, enRevision.getEstado());
    }

    @Test
    void enviarARevision_pagoInexistente_lanzaExcepcion() {
        assertThrows(RecursoNoEncontradoException.class, () ->
                service.enviarARevision("pago-inexistente"));
    }

    @Test
    void aprobarPago_desdePendiente_cambiaAAprobado() {
        Pago pago = service.subirComprobante(equipo.getId(), "comprobante.jpg");
        Pago aprobado = service.aprobarPago(pago.getId());
        assertEquals(Pago.PagoEstado.APROBADO, aprobado.getEstado());
    }

    @Test
    void aprobarPago_pagoInexistente_lanzaExcepcion() {
        assertThrows(RecursoNoEncontradoException.class, () ->
                service.aprobarPago("pago-inexistente"));
    }

    @Test
    void rechazarPago_desdeEnRevision_cambiaARechazado() {
        Pago pago = service.subirComprobante(equipo.getId(), "comprobante.jpg");
        service.enviarARevision(pago.getId());
        Pago rechazado = service.rechazarPago(pago.getId());
        assertEquals(Pago.PagoEstado.RECHAZADO, rechazado.getEstado());
    }

    @Test
    void rechazarPago_desdePendiente_lanzaExcepcion() {
        Pago pago = service.subirComprobante(equipo.getId(), "comprobante.jpg");
        assertThrows(IllegalStateException.class, () -> service.rechazarPago(pago.getId()));
    }

    @Test
    void rechazarPago_pagoInexistente_lanzaExcepcion() {
        assertThrows(RecursoNoEncontradoException.class, () ->
                service.rechazarPago("pago-inexistente"));
    }

    @Test
    void consultarPago_existente_retornaPago() {
        Pago pago = service.subirComprobante(equipo.getId(), "comprobante.jpg");
        assertNotNull(service.consultarPago(pago.getId()));
    }

    @Test
    void consultarPago_inexistente_lanzaExcepcion() {
        assertThrows(RecursoNoEncontradoException.class, () ->
                service.consultarPago("pago-inexistente"));
    }

    @Test
    void consultarPagosPorEquipo_retornaLista() {
        service.subirComprobante(equipo.getId(), "comprobante.jpg");
        assertEquals(1, service.consultarPagosPorEquipo(equipo.getId()).size());
    }

    @Test
    void consultarPagosPorEquipo_sinPagos_retornaListaVacia() {
        assertTrue(service.consultarPagosPorEquipo(equipo.getId()).isEmpty());
    }

    @Test
    void consultarPagosPendientes_retornaLista() {
        service.subirComprobante(equipo.getId(), "comprobante.jpg");
        assertFalse(service.consultarPagosPendientes().isEmpty());
    }

    @Test
    void consultarPagosPorEstado_enRevision_retornaLista() {
        Pago pago = service.subirComprobante(equipo.getId(), "comprobante.jpg");
        service.enviarARevision(pago.getId());
        List<Pago> enRevision = service.consultarPagosPorEstado(Pago.PagoEstado.EN_REVISION);
        assertFalse(enRevision.isEmpty());
        assertEquals(Pago.PagoEstado.EN_REVISION, enRevision.get(0).getEstado());
    }

    @Test
    void consultarPagosPorEstado_aprobado_retornaLista() {
        Pago pago = service.subirComprobante(equipo.getId(), "comprobante.jpg");
        service.aprobarPago(pago.getId());
        List<Pago> aprobados = service.consultarPagosPorEstado(Pago.PagoEstado.APROBADO);
        assertFalse(aprobados.isEmpty());
    }

    @Test
    void equipoHabilitado_sinPagoAprobado_retornaFalse() {
        assertFalse(service.equipoHabilitado(equipo.getId()));
    }

    @Test
    void equipoHabilitado_conPagoAprobado_retornaTrue() {
        Pago pago = service.subirComprobante(equipo.getId(), "comprobante.jpg");
        service.aprobarPago(pago.getId());
        assertTrue(service.equipoHabilitado(equipo.getId()));
    }

    @Test
    void flujoCompleto_registro_revision_aprobacion() {
        Pago pago = service.subirComprobante(equipo.getId(), "comprobante.jpg");
        assertEquals(Pago.PagoEstado.PENDIENTE, pago.getEstado());

        Pago enRevision = service.enviarARevision(pago.getId());
        assertEquals(Pago.PagoEstado.EN_REVISION, enRevision.getEstado());

        Pago aprobado = service.aprobarPago(pago.getId());
        assertEquals(Pago.PagoEstado.APROBADO, aprobado.getEstado());

        assertTrue(service.equipoHabilitado(equipo.getId()));
    }

    @Test
    void flujoCompleto_registro_revision_rechazo() {
        Pago pago = service.subirComprobante(equipo.getId(), "comprobante.jpg");
        service.enviarARevision(pago.getId());
        Pago rechazado = service.rechazarPago(pago.getId());
        assertEquals(Pago.PagoEstado.RECHAZADO, rechazado.getEstado());
        assertFalse(service.equipoHabilitado(equipo.getId()));
    }
}
