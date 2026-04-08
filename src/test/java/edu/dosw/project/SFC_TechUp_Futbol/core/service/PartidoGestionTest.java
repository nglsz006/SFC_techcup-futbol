package edu.dosw.project.SFC_TechUp_Futbol.core.service;

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

class PartidoGestionTest {

    private PartidoServiceImpl service;
    private JugadorJpaRepository jugadorRepo;
    private JugadorMapper jugadorMapper;
    private TorneoJpaRepository torneoRepo;
    private TorneoMapper torneoMapper;
    private Torneo torneo;
    private Equipo local;
    private Equipo visitante;

    @BeforeEach
    void setUp() {
        torneoMapper = new TorneoMapper();
        EquipoMapper equipoMapper = new EquipoMapper();
        jugadorMapper = new JugadorMapper();
        PartidoMapper partidoMapper = new PartidoMapper(torneoMapper, equipoMapper, jugadorMapper);

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
        EquipoJpaRepository equipoRepo = mock(EquipoJpaRepository.class);
        when(equipoRepo.save(any())).thenAnswer(inv -> {
            EquipoEntity e = inv.getArgument(0);
            if (e.getId() == null) e.setId(UUID.randomUUID().toString());
            equipoStore.put(e.getId(), e);
            return e;
        });
        when(equipoRepo.findById(anyString())).thenAnswer(inv -> Optional.ofNullable(equipoStore.get(inv.<String>getArgument(0))));

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
        PartidoJpaRepository partidoRepo = mock(PartidoJpaRepository.class);
        when(partidoRepo.save(any())).thenAnswer(inv -> {
            PartidoEntity e = inv.getArgument(0);
            if (e.getId() == null) e.setId(UUID.randomUUID().toString());
            partidoStore.put(e.getId(), e);
            return e;
        });
        when(partidoRepo.findById(anyString())).thenAnswer(inv -> Optional.ofNullable(partidoStore.get(inv.<String>getArgument(0))));
        when(partidoRepo.findByTorneoId(anyString())).thenAnswer(inv -> {
            String tid = inv.getArgument(0);
            return partidoStore.values().stream()
                    .filter(e -> e.getTorneo() != null && tid.equals(e.getTorneo().getId()))
                    .collect(Collectors.toList());
        });
        when(partidoRepo.findByEstado(any())).thenAnswer(inv -> {
            Partido.PartidoEstado estado = inv.getArgument(0);
            return partidoStore.values().stream()
                    .filter(e -> e.getEstado() == estado)
                    .collect(Collectors.toList());
        });
        when(partidoRepo.findByEquipoLocalIdOrEquipoVisitanteId(anyString(), anyString())).thenAnswer(inv -> {
            String lid = inv.getArgument(0);
            String vid = inv.getArgument(1);
            return partidoStore.values().stream().filter(e ->
                    (e.getEquipoLocal() != null && lid.equals(e.getEquipoLocal().getId())) ||
                    (e.getEquipoVisitante() != null && vid.equals(e.getEquipoVisitante().getId()))
            ).collect(Collectors.toList());
        });

        service = new PartidoServiceImpl(partidoRepo, partidoMapper, torneoRepo, torneoMapper, equipoRepo, equipoMapper, jugadorRepo, jugadorMapper);

        TorneoEntity te = torneoRepo.save(torneoMapper.toEntity(new Torneo(null, "Copa Tecnica", LocalDateTime.now(), LocalDateTime.now().plusDays(10), 8, 50)));
        torneo = torneoMapper.toDomain(te);
        EquipoEntity le = equipoRepo.save(equipoMapper.toEntity(new Equipo(null, "Los Tigres", "", "rojo", "blanco", "cap-1")));
        local = equipoMapper.toDomain(le);
        EquipoEntity ve = equipoRepo.save(equipoMapper.toEntity(new Equipo(null, "Los Leones", "", "azul", "negro", "cap-2")));
        visitante = equipoMapper.toDomain(ve);
    }

    // --- Creacion de partido dentro del torneo ---

    @Test
    void crearPartido_dentroDelTorneo_quedaAsociadoAlTorneo() {
        Partido p = service.crearPartido(torneo.getId(), local.getId(), visitante.getId(), LocalDateTime.now(), "cancha 3");
        assertNotNull(p.getId());
        assertNotNull(p.getTorneo());
        assertEquals(torneo.getId(), p.getTorneo().getId());
    }

    @Test
    void crearPartido_estadoInicialEsProgramado() {
        Partido p = service.crearPartido(torneo.getId(), local.getId(), visitante.getId(), LocalDateTime.now(), "cancha 3");
        assertEquals(Partido.PartidoEstado.PROGRAMADO, p.getEstado());
    }

    @Test
    void crearPartido_torneoNoExiste_lanzaExcepcion() {
        assertThrows(RuntimeException.class, () ->
                service.crearPartido("torneo-inexistente", local.getId(), visitante.getId(), LocalDateTime.now(), "cancha 3"));
    }

    @Test
    void crearPartido_torneoFinalizado_lanzaExcepcion() {
        Torneo torneoFinalizado = new Torneo(null, "Copa Fin", LocalDateTime.now(), LocalDateTime.now().plusDays(5), 8, 50);
        torneoFinalizado.iniciar();
        torneoFinalizado.finalizar();
        TorneoEntity te = torneoRepo.save(torneoMapper.toEntity(torneoFinalizado));
        assertThrows(IllegalStateException.class, () ->
                service.crearPartido(te.getId(), local.getId(), visitante.getId(), LocalDateTime.now(), "cancha 3"));
    }

    @Test
    void crearPartido_fechaNula_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () ->
                service.crearPartido(torneo.getId(), local.getId(), visitante.getId(), null, "cancha 3"));
    }

    @Test
    void crearPartido_canchaVacia_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () ->
                service.crearPartido(torneo.getId(), local.getId(), visitante.getId(), LocalDateTime.now(), ""));
    }

    // --- Validacion de equipos local y visitante ---

    @Test
    void crearPartido_equipoLocalNoExiste_lanzaExcepcion() {
        assertThrows(RuntimeException.class, () ->
                service.crearPartido(torneo.getId(), "equipo-inexistente", visitante.getId(), LocalDateTime.now(), "cancha 3"));
    }

    @Test
    void crearPartido_equipoVisitanteNoExiste_lanzaExcepcion() {
        assertThrows(RuntimeException.class, () ->
                service.crearPartido(torneo.getId(), local.getId(), "equipo-inexistente", LocalDateTime.now(), "cancha 3"));
    }

    @Test
    void crearPartido_mismoEquipoLocalYVisitante_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () ->
                service.crearPartido(torneo.getId(), local.getId(), local.getId(), LocalDateTime.now(), "cancha 3"));
    }

    @Test
    void crearPartido_equiposDistintos_asignaCorrectamente() {
        Partido p = service.crearPartido(torneo.getId(), local.getId(), visitante.getId(), LocalDateTime.now(), "cancha 3");
        assertEquals(local.getId(), p.getEquipoLocal().getId());
        assertEquals(visitante.getId(), p.getEquipoVisitante().getId());
    }

    // --- Programacion de fecha, hora y cancha ---

    @Test
    void crearPartido_fechaYCanchaQuedan_persistidos() {
        LocalDateTime fecha = LocalDateTime.of(2025, 8, 15, 16, 0);
        Partido p = service.crearPartido(torneo.getId(), local.getId(), visitante.getId(), fecha, "cancha principal");
        assertEquals(fecha, p.getFecha());
        assertEquals("cancha principal", p.getCancha());
    }

    @Test
    void consultarPartido_despuesDeCrear_retornaInformacionCompleta() {
        LocalDateTime fecha = LocalDateTime.of(2025, 8, 20, 18, 30);
        Partido creado = service.crearPartido(torneo.getId(), local.getId(), visitante.getId(), fecha, "cancha 2");
        Partido consultado = service.consultarPartido(creado.getId());
        assertEquals(creado.getId(), consultado.getId());
        assertEquals(fecha, consultado.getFecha());
        assertEquals("cancha 2", consultado.getCancha());
    }

    // --- Ciclo de estados del partido ---

    @Test
    void iniciarPartido_desdeProgramado_cambaAEnCurso() {
        Partido p = service.crearPartido(torneo.getId(), local.getId(), visitante.getId(), LocalDateTime.now(), "cancha 3");
        Partido iniciado = service.iniciarPartido(p.getId());
        assertEquals(Partido.PartidoEstado.EN_CURSO, iniciado.getEstado());
    }

    @Test
    void finalizarPartido_desdeEnCurso_cambaAFinalizado() {
        Partido p = service.crearPartido(torneo.getId(), local.getId(), visitante.getId(), LocalDateTime.now(), "cancha 3");
        service.iniciarPartido(p.getId());
        Partido finalizado = service.finalizarPartido(p.getId());
        assertEquals(Partido.PartidoEstado.FINALIZADO, finalizado.getEstado());
    }

    @Test
    void iniciarPartido_yaEnCurso_lanzaExcepcion() {
        Partido p = service.crearPartido(torneo.getId(), local.getId(), visitante.getId(), LocalDateTime.now(), "cancha 3");
        service.iniciarPartido(p.getId());
        assertThrows(IllegalStateException.class, () -> service.iniciarPartido(p.getId()));
    }

    @Test
    void finalizarPartido_yaFinalizado_lanzaExcepcion() {
        Partido p = service.crearPartido(torneo.getId(), local.getId(), visitante.getId(), LocalDateTime.now(), "cancha 3");
        service.iniciarPartido(p.getId());
        service.finalizarPartido(p.getId());
        assertThrows(IllegalStateException.class, () -> service.finalizarPartido(p.getId()));
    }

    @Test
    void finalizarPartido_desdeProgramado_lanzaExcepcion() {
        Partido p = service.crearPartido(torneo.getId(), local.getId(), visitante.getId(), LocalDateTime.now(), "cancha 3");
        assertThrows(IllegalStateException.class, () -> service.finalizarPartido(p.getId()));
    }

    @Test
    void iniciarPartido_inexistente_lanzaExcepcion() {
        assertThrows(RuntimeException.class, () -> service.iniciarPartido("partido-inexistente"));
    }

    // --- Consulta operativa de partidos ---

    @Test
    void consultarPartidosPorTorneo_retornaPartidosDelTorneo() {
        service.crearPartido(torneo.getId(), local.getId(), visitante.getId(), LocalDateTime.now(), "cancha 3");
        service.crearPartido(torneo.getId(), local.getId(), visitante.getId(), LocalDateTime.now().plusHours(2), "cancha 4");
        assertEquals(2, service.consultarPartidosPorTorneo(torneo.getId()).size());
    }

    @Test
    void consultarPartidosPorTorneo_torneoSinPartidos_retornaListaVacia() {
        assertTrue(service.consultarPartidosPorTorneo(torneo.getId()).isEmpty());
    }

    @Test
    void consultarPartidosPorEquipo_retornaPartidosDelEquipo() {
        service.crearPartido(torneo.getId(), local.getId(), visitante.getId(), LocalDateTime.now(), "cancha 3");
        assertFalse(service.consultarPartidosPorEquipo(local.getId()).isEmpty());
    }

    @Test
    void consultarPartidosPorEquipo_equipoSinPartidos_retornaListaVacia() {
        assertTrue(service.consultarPartidosPorEquipo("equipo-sin-partidos").isEmpty());
    }

    @Test
    void consultarPartidosPorEstado_programado_retornaPartidosEnEseEstado() {
        service.crearPartido(torneo.getId(), local.getId(), visitante.getId(), LocalDateTime.now(), "cancha 3");
        assertFalse(service.consultarPartidosPorEstado(Partido.PartidoEstado.PROGRAMADO).isEmpty());
    }

    @Test
    void consultarPartidosPorEstado_sinPartidosEnEseEstado_retornaListaVacia() {
        service.crearPartido(torneo.getId(), local.getId(), visitante.getId(), LocalDateTime.now(), "cancha 3");
        assertTrue(service.consultarPartidosPorEstado(Partido.PartidoEstado.FINALIZADO).isEmpty());
    }

    @Test
    void consultarPartido_inexistente_lanzaExcepcion() {
        assertThrows(RuntimeException.class, () -> service.consultarPartido("partido-inexistente"));
    }

    // --- Integridad funcional del modulo ---

    @Test
    void flujoCompleto_crear_iniciar_registrarResultado_finalizar() {
        Partido p = service.crearPartido(torneo.getId(), local.getId(), visitante.getId(), LocalDateTime.now(), "cancha 3");
        assertEquals(Partido.PartidoEstado.PROGRAMADO, p.getEstado());

        Partido iniciado = service.iniciarPartido(p.getId());
        assertEquals(Partido.PartidoEstado.EN_CURSO, iniciado.getEstado());

        Partido conResultado = service.registrarResultado(p.getId(), 3, 1);
        assertEquals(3, conResultado.getMarcadorLocal());
        assertEquals(1, conResultado.getMarcadorVisitante());

        Partido finalizado = service.finalizarPartido(p.getId());
        assertEquals(Partido.PartidoEstado.FINALIZADO, finalizado.getEstado());
    }

    @Test
    void registrarResultado_marcadorNegativo_lanzaExcepcion() {
        Partido p = service.crearPartido(torneo.getId(), local.getId(), visitante.getId(), LocalDateTime.now(), "cancha 3");
        service.iniciarPartido(p.getId());
        assertThrows(IllegalArgumentException.class, () -> service.registrarResultado(p.getId(), -1, 0));
    }

    @Test
    void registrarGoleador_minutoFueraDeRango_lanzaExcepcion() {
        Partido p = service.crearPartido(torneo.getId(), local.getId(), visitante.getId(), LocalDateTime.now(), "cancha 3");
        service.iniciarPartido(p.getId());
        assertThrows(IllegalArgumentException.class, () -> service.registrarGoleador(p.getId(), "jugador-1", 0));
    }

    @Test
    void registrarGoleador_jugadorIdVacio_lanzaExcepcion() {
        Partido p = service.crearPartido(torneo.getId(), local.getId(), visitante.getId(), LocalDateTime.now(), "cancha 3");
        service.iniciarPartido(p.getId());
        assertThrows(IllegalArgumentException.class, () -> service.registrarGoleador(p.getId(), "", 30));
    }

    @Test
    void registrarGoleador_enCurso_incrementaMarcadorYAgregaGol() {
        Jugador jugador = new Jugador(null, "Carlos", "carlos@test.com", "pass", Usuario.TipoUsuario.ESTUDIANTE, 9, Jugador.Posicion.DELANTERO, true, local.getId());
        JugadorEntity saved = jugadorRepo.save(jugadorMapper.toEntity(jugador));
        jugador.setId(saved.getId());

        Partido p = service.crearPartido(torneo.getId(), local.getId(), visitante.getId(), LocalDateTime.now(), "cancha 3");
        service.iniciarPartido(p.getId());
        Partido conGol = service.registrarGoleador(p.getId(), jugador.getId(), 45);
        assertEquals(1, conGol.getGoles().size());
    }

    @Test
    void registrarGoleador_partidoNoEnCurso_lanzaExcepcion() {
        Jugador jugador = new Jugador(null, "Carlos", "carlos@test.com", "pass", Usuario.TipoUsuario.ESTUDIANTE, 9, Jugador.Posicion.DELANTERO, true, local.getId());
        JugadorEntity saved = jugadorRepo.save(jugadorMapper.toEntity(jugador));
        jugador.setId(saved.getId());

        Partido p = service.crearPartido(torneo.getId(), local.getId(), visitante.getId(), LocalDateTime.now(), "cancha 3");
        assertThrows(IllegalStateException.class, () -> service.registrarGoleador(p.getId(), jugador.getId(), 45));
    }
}
