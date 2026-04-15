package edu.dosw.project.SFC_TechUp_Futbol.core.service;

import edu.dosw.project.SFC_TechUp_Futbol.TestMappers;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.*;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.entity.*;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.mapper.*;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PartidoServiceAvanzarTest {

    private PartidoServiceImpl service;
    private PartidoJpaRepository partidoRepo;
    private TorneoJpaRepository torneoRepo;
    private EquipoJpaRepository equipoRepo;
    private JugadorJpaRepository jugadorRepo;
    private PartidoMapper partidoMapper;
    private TorneoMapper torneoMapper;
    private EquipoMapper equipoMapper;
    private JugadorMapper jugadorMapper;
    private Torneo torneo;
    private Equipo local;
    private Equipo visitante;

    @BeforeEach
    void setUp() {
        torneoMapper = TestMappers.torneoMapper();
        equipoMapper = TestMappers.equipoMapper();
        jugadorMapper = TestMappers.jugadorMapper();
        partidoMapper = TestMappers.partidoMapper(jugadorMapper);

        Map<String, TorneoEntity> torneoStore = new HashMap<>();
        torneoRepo = mock(TorneoJpaRepository.class);
        when(torneoRepo.save(any())).thenAnswer(inv -> {
            TorneoEntity e = inv.getArgument(0);
            if (e.getId() == null) e.setId(UUID.randomUUID().toString());
            torneoStore.put(e.getId(), e);
            return e;
        });
        when(torneoRepo.findById(anyString())).thenAnswer(inv -> Optional.ofNullable(torneoStore.get(inv.<String>getArgument(0))));

        Map<String, EquipoEntity> equipoStore = new HashMap<>();
        equipoRepo = mock(EquipoJpaRepository.class);
        when(equipoRepo.save(any())).thenAnswer(inv -> {
            EquipoEntity e = inv.getArgument(0);
            if (e.getId() == null) e.setId(UUID.randomUUID().toString());
            equipoStore.put(e.getId(), e);
            return e;
        });
        when(equipoRepo.findById(anyString())).thenAnswer(inv -> Optional.ofNullable(equipoStore.get(inv.<String>getArgument(0))));
        when(equipoRepo.findAll()).thenAnswer(inv -> new ArrayList<>(equipoStore.values()));

        Map<String, JugadorEntity> jugadorStore = new HashMap<>();
        jugadorRepo = mock(JugadorJpaRepository.class);
        when(jugadorRepo.save(any())).thenAnswer(inv -> {
            JugadorEntity e = inv.getArgument(0);
            if (e.getId() == null) e.setId(UUID.randomUUID().toString());
            jugadorStore.put(e.getId(), e);
            return e;
        });
        when(jugadorRepo.findById(anyString())).thenAnswer(inv -> Optional.ofNullable(jugadorStore.get(inv.<String>getArgument(0))));

        Map<String, PartidoEntity> partidoStore = new HashMap<>();
        partidoRepo = mock(PartidoJpaRepository.class);
        when(partidoRepo.save(any())).thenAnswer(inv -> {
            PartidoEntity e = inv.getArgument(0);
            if (e.getId() == null) e.setId(UUID.randomUUID().toString());
            partidoStore.put(e.getId(), e);
            return e;
        });
        when(partidoRepo.findById(anyString())).thenAnswer(inv -> Optional.ofNullable(partidoStore.get(inv.<String>getArgument(0))));
        when(partidoRepo.findByTorneoId(anyString())).thenAnswer(inv ->
                partidoStore.values().stream().filter(e -> e.getTorneo() != null && inv.<String>getArgument(0).equals(e.getTorneo().getId())).collect(Collectors.toList()));
        when(partidoRepo.findByEstado(any())).thenAnswer(inv ->
                partidoStore.values().stream().filter(e -> e.getEstado() == inv.<Partido.PartidoEstado>getArgument(0)).collect(Collectors.toList()));
        when(partidoRepo.findByEquipoLocalIdOrEquipoVisitanteId(anyString(), anyString())).thenAnswer(inv -> {
            String lid = inv.getArgument(0); String vid = inv.getArgument(1);
            return partidoStore.values().stream().filter(e ->
                    (e.getEquipoLocal() != null && lid.equals(e.getEquipoLocal().getId())) ||
                    (e.getEquipoVisitante() != null && vid.equals(e.getEquipoVisitante().getId()))
            ).collect(Collectors.toList());
        });
        when(partidoRepo.findByTorneoIdAndFase(anyString(), any())).thenAnswer(inv -> {
            String tid = inv.getArgument(0); Partido.Fase fase = inv.getArgument(1);
            return partidoStore.values().stream().filter(e ->
                    e.getTorneo() != null && tid.equals(e.getTorneo().getId()) && fase == e.getFase()
            ).collect(Collectors.toList());
        });

        service = new PartidoServiceImpl(partidoRepo, partidoMapper, torneoRepo, torneoMapper, equipoRepo, equipoMapper, jugadorRepo, jugadorMapper);

        TorneoEntity te = torneoRepo.save(torneoMapper.toEntity(new Torneo(null, "Copa", LocalDateTime.now(), LocalDateTime.now().plusDays(10), 8, 50)));
        torneo = torneoMapper.toDomain(te);
        EquipoEntity le = equipoRepo.save(equipoMapper.toEntity(new Equipo(null, "Local", "", "rojo", "blanco", "cap-1")));
        local = equipoMapper.toDomain(le);
        EquipoEntity ve = equipoRepo.save(equipoMapper.toEntity(new Equipo(null, "Visitante", "", "azul", "negro", "cap-2")));
        visitante = equipoMapper.toDomain(ve);
    }

    @Test
    void finalizarPartido_sinFase_noLlamaAvanzarGanador() {
        Partido p = service.crearPartido(torneo.getId(), local.getId(), visitante.getId(), LocalDateTime.now(), "cancha");
        service.iniciarPartido(p.getId());
        service.registrarResultado(p.getId(), 2, 1);
        Partido finalizado = service.finalizarPartido(p.getId());
        assertEquals(Partido.PartidoEstado.FINALIZADO, finalizado.getEstado());
    }

    @Test
    void maximosGoleadores_sinGoles_retornaListaVacia() {
        assertTrue(service.maximosGoleadores(torneo.getId()).isEmpty());
    }

    @Test
    void maximosGoleadores_conGoles_retornaOrdenado() {
        Jugador j1 = new Jugador(null, "Goleador", "g@test.com", "pass", Usuario.TipoUsuario.ESTUDIANTE, 9, Jugador.Posicion.DELANTERO, true, local.getId());
        JugadorEntity je1 = jugadorRepo.save(jugadorMapper.toEntity(j1));
        j1.setId(je1.getId());

        Partido p = service.crearPartido(torneo.getId(), local.getId(), visitante.getId(), LocalDateTime.now(), "cancha");
        service.iniciarPartido(p.getId());
        service.registrarGoleador(p.getId(), j1.getId(), 10);
        service.registrarGoleador(p.getId(), j1.getId(), 20);

        List<Map<String, Object>> goleadores = service.maximosGoleadores(torneo.getId());
        assertFalse(goleadores.isEmpty());
        assertEquals(2, goleadores.get(0).get("goles"));
    }

    @Test
    void generarLlaves_pocosEquipos_lanzaExcepcion() {
        assertThrows(IllegalStateException.class, () -> service.generarLlaves(torneo.getId()));
    }

    @Test
    void generarLlaves_torneoFinalizado_lanzaExcepcion() {
        torneo.iniciar();
        torneo.finalizar();
        TorneoEntity te = torneoRepo.save(torneoMapper.toEntity(torneo));
        assertThrows(IllegalStateException.class, () -> service.generarLlaves(te.getId()));
    }

    @Test
    void generarLlaves_conEquiposYPartidosFinalizados_creaPartidosCuartos() {
        // Crear 8 equipos adicionales con partidos finalizados para la tabla
        List<Equipo> equipos = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            EquipoEntity ee = equipoRepo.save(equipoMapper.toEntity(new Equipo(null, "Equipo" + i, "", "rojo", "blanco", "cap" + i)));
            equipos.add(equipoMapper.toDomain(ee));
        }
        // Crear y finalizar partidos para que aparezcan en la tabla
        for (int i = 0; i < 4; i++) {
            Partido p = service.crearPartido(torneo.getId(), equipos.get(i * 2).getId(), equipos.get(i * 2 + 1).getId(), LocalDateTime.now(), "cancha");
            service.iniciarPartido(p.getId());
            service.registrarResultado(p.getId(), 2, 1);
            service.finalizarPartido(p.getId());
        }
        List<Partido> llaves = service.generarLlaves(torneo.getId());
        assertFalse(llaves.isEmpty());
        llaves.forEach(p -> assertEquals(Partido.Fase.CUARTOS, p.getFase()));
    }

    @Test
    void consultarPartidosPorEstado_programado_retornaLista() {
        service.crearPartido(torneo.getId(), local.getId(), visitante.getId(), LocalDateTime.now(), "cancha");
        assertFalse(service.consultarPartidosPorEstado(Partido.PartidoEstado.PROGRAMADO).isEmpty());
    }

    @Test
    void consultarEventos_retornaGolesTarjetasSanciones() {
        Partido p = service.crearPartido(torneo.getId(), local.getId(), visitante.getId(), LocalDateTime.now(), "cancha");
        service.iniciarPartido(p.getId());
        Map<String, Object> eventos = service.consultarEventos(p.getId());
        assertNotNull(eventos.get("goles"));
        assertNotNull(eventos.get("tarjetas"));
        assertNotNull(eventos.get("sanciones"));
    }
}
