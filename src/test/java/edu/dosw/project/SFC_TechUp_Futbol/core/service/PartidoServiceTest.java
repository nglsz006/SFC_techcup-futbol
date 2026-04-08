package edu.dosw.project.SFC_TechUp_Futbol.core.service;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.*;
import edu.dosw.project.SFC_TechUp_Futbol.core.service.PartidoServiceImpl;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.entity.*;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.mapper.*;
import edu.dosw.project.SFC_TechUp_Futbol.TestMappers;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PartidoServiceTest {

    private PartidoServiceImpl service;
    private JugadorJpaRepository jugadorRepo;
    private JugadorMapper jugadorMapper;
    private Torneo torneo;
    private Equipo local;
    private Equipo visitante;

    @BeforeEach
    void setUp() {
        TorneoMapper torneoMapper = TestMappers.torneoMapper();
        EquipoMapper equipoMapper = TestMappers.equipoMapper();
        jugadorMapper = TestMappers.jugadorMapper();
        PartidoMapper partidoMapper = TestMappers.partidoMapper(jugadorMapper);

        Map<String, TorneoEntity> torneoStore = new HashMap<>();
        TorneoJpaRepository torneoRepo = mock(TorneoJpaRepository.class);
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
            return partidoStore.values().stream().filter(e -> e.getTorneo() != null && tid.equals(e.getTorneo().getId())).collect(Collectors.toList());
        });
        when(partidoRepo.findByEstado(any())).thenAnswer(inv -> {
            Partido.PartidoEstado estado = inv.getArgument(0);
            return partidoStore.values().stream().filter(e -> e.getEstado() == estado).collect(Collectors.toList());
        });
        when(partidoRepo.findByEquipoLocalIdOrEquipoVisitanteId(anyString(), anyString())).thenAnswer(inv -> {
            String lid = inv.getArgument(0); String vid = inv.getArgument(1);
            return partidoStore.values().stream().filter(e ->
                    (e.getEquipoLocal() != null && lid.equals(e.getEquipoLocal().getId())) ||
                    (e.getEquipoVisitante() != null && vid.equals(e.getEquipoVisitante().getId()))
            ).collect(Collectors.toList());
        });

        service = new PartidoServiceImpl(partidoRepo, partidoMapper, torneoRepo, torneoMapper, equipoRepo, equipoMapper, jugadorRepo, jugadorMapper);

        TorneoEntity te = torneoRepo.save(torneoMapper.toEntity(new Torneo(null, "Copa", LocalDateTime.now(), LocalDateTime.now().plusDays(5), 8, 50)));
        torneo = torneoMapper.toDomain(te);
        EquipoEntity le = equipoRepo.save(equipoMapper.toEntity(new Equipo(null, "Local", "", "rojo", "blanco", "uuid-cap-1")));
        local = equipoMapper.toDomain(le);
        EquipoEntity ve = equipoRepo.save(equipoMapper.toEntity(new Equipo(null, "Visitante", "", "azul", "negro", "uuid-cap-2")));
        visitante = equipoMapper.toDomain(ve);
    }

    @Test
    void crearPartido_datosValidos_retornaPartido() {
        Partido p = service.crearPartido(torneo.getId(), local.getId(), visitante.getId(), LocalDateTime.now(), "cancha 1");
        assertNotNull(p.getId());
        assertEquals(Partido.PartidoEstado.PROGRAMADO, p.getEstado());
    }

    @Test
    void crearPartido_mismoEquipo_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () ->
                service.crearPartido(torneo.getId(), local.getId(), local.getId(), LocalDateTime.now(), "cancha 1"));
    }

    @Test
    void crearPartido_torneoInexistente_lanzaExcepcion() {
        assertThrows(RuntimeException.class, () ->
                service.crearPartido("uuid-inexistente", local.getId(), visitante.getId(), LocalDateTime.now(), "cancha 1"));
    }

    @Test
    void iniciarPartido_estadoProgramado_cambaAEnCurso() {
        Partido p = service.crearPartido(torneo.getId(), local.getId(), visitante.getId(), LocalDateTime.now(), "cancha 1");
        Partido iniciado = service.iniciarPartido(p.getId());
        assertEquals(Partido.PartidoEstado.EN_CURSO, iniciado.getEstado());
    }

    @Test
    void registrarResultado_enCurso_actualizaMarcador() {
        Partido p = service.crearPartido(torneo.getId(), local.getId(), visitante.getId(), LocalDateTime.now(), "cancha 1");
        service.iniciarPartido(p.getId());
        Partido resultado = service.registrarResultado(p.getId(), 2, 1);
        assertEquals(2, resultado.getMarcadorLocal());
        assertEquals(1, resultado.getMarcadorVisitante());
    }

    @Test
    void finalizarPartido_enCurso_cambaAFinalizado() {
        Partido p = service.crearPartido(torneo.getId(), local.getId(), visitante.getId(), LocalDateTime.now(), "cancha 1");
        service.iniciarPartido(p.getId());
        Partido finalizado = service.finalizarPartido(p.getId());
        assertEquals(Partido.PartidoEstado.FINALIZADO, finalizado.getEstado());
    }

    @Test
    void registrarGoleador_enCurso_agregaGol() {
        Jugador jugador = new Jugador(null, "Juan", "juan@test.com", "pass", Usuario.TipoUsuario.ESTUDIANTE, 10, Jugador.Posicion.DELANTERO, true, "");
        JugadorEntity saved = jugadorRepo.save(jugadorMapper.toEntity(jugador));
        jugador.setId(saved.getId());
        Partido p = service.crearPartido(torneo.getId(), local.getId(), visitante.getId(), LocalDateTime.now(), "cancha 1");
        service.iniciarPartido(p.getId());
        Partido con_gol = service.registrarGoleador(p.getId(), jugador.getId(), 30);
        assertEquals(1, con_gol.getGoles().size());
    }

    @Test
    void registrarGoleador_noEnCurso_lanzaExcepcion() {
        Jugador jugador = new Jugador(null, "Juan", "juan@test.com", "pass", Usuario.TipoUsuario.ESTUDIANTE, 10, Jugador.Posicion.DELANTERO, true, "");
        JugadorEntity saved = jugadorRepo.save(jugadorMapper.toEntity(jugador));
        jugador.setId(saved.getId());
        Partido p = service.crearPartido(torneo.getId(), local.getId(), visitante.getId(), LocalDateTime.now(), "cancha 1");
        assertThrows(IllegalStateException.class, () -> service.registrarGoleador(p.getId(), jugador.getId(), 30));
    }

    @Test
    void registrarSancion_enCurso_agregaSancionAlPartidoYAlJugador() {
        Jugador jugador = new Jugador(null, "Juan", "juan@test.com", "pass", Usuario.TipoUsuario.ESTUDIANTE, 10, Jugador.Posicion.DELANTERO, true, "");
        JugadorEntity saved = jugadorRepo.save(jugadorMapper.toEntity(jugador));
        jugador.setId(saved.getId());
        Partido p = service.crearPartido(torneo.getId(), local.getId(), visitante.getId(), LocalDateTime.now(), "cancha 1");
        service.iniciarPartido(p.getId());
        Partido conSancion = service.registrarSancion(p.getId(), jugador.getId(), Sancion.TipoSancion.TARJETA_AMARILLA, "Falta reiterada sobre el rival");
        assertEquals(1, conSancion.getSanciones().size());
        assertEquals(Sancion.TipoSancion.TARJETA_AMARILLA, conSancion.getSanciones().get(0).getTipoSancion());
    }

    @Test
    void registrarSancion_noEnCurso_lanzaExcepcion() {
        Jugador jugador = new Jugador(null, "Juan", "juan@test.com", "pass", Usuario.TipoUsuario.ESTUDIANTE, 10, Jugador.Posicion.DELANTERO, true, "");
        JugadorEntity saved = jugadorRepo.save(jugadorMapper.toEntity(jugador));
        jugador.setId(saved.getId());
        Partido p = service.crearPartido(torneo.getId(), local.getId(), visitante.getId(), LocalDateTime.now(), "cancha 1");
        assertThrows(IllegalStateException.class, () ->
                service.registrarSancion(p.getId(), jugador.getId(), Sancion.TipoSancion.TARJETA_ROJA, "Juego brusco"));
    }

    @Test
    void registrarSancion_agresionVerbal_agregaCorrectamente() {
        Jugador jugador = new Jugador(null, "Juan", "juan@test.com", "pass", Usuario.TipoUsuario.ESTUDIANTE, 10, Jugador.Posicion.DELANTERO, true, "");
        JugadorEntity saved = jugadorRepo.save(jugadorMapper.toEntity(jugador));
        jugador.setId(saved.getId());
        Partido p = service.crearPartido(torneo.getId(), local.getId(), visitante.getId(), LocalDateTime.now(), "cancha 1");
        service.iniciarPartido(p.getId());
        Partido conSancion = service.registrarSancion(p.getId(), jugador.getId(), Sancion.TipoSancion.AGRESION_VERBAL, "Insultos al árbitro en el minuto 35");
        assertTrue(conSancion.getSanciones().stream().anyMatch(s -> s.getTipoSancion() == Sancion.TipoSancion.AGRESION_VERBAL));
    }

    @Test
    void registrarSancion_multiplesSancionesAlMismoJugador_acumula() {
        Jugador jugador = new Jugador(null, "Juan", "juan@test.com", "pass", Usuario.TipoUsuario.ESTUDIANTE, 10, Jugador.Posicion.DELANTERO, true, "");
        JugadorEntity saved = jugadorRepo.save(jugadorMapper.toEntity(jugador));
        jugador.setId(saved.getId());
        Partido p = service.crearPartido(torneo.getId(), local.getId(), visitante.getId(), LocalDateTime.now(), "cancha 1");
        service.iniciarPartido(p.getId());
        Partido con1 = service.registrarSancion(p.getId(), jugador.getId(), Sancion.TipoSancion.TARJETA_AMARILLA, "Primera falta en minuto 20");
        Partido con2 = service.registrarSancion(p.getId(), jugador.getId(), Sancion.TipoSancion.TARJETA_AMARILLA, "Segunda falta en minuto 60");
        assertEquals(1, con1.getSanciones().size());
        assertEquals(2, con2.getSanciones().size());
    }

    @Test
    void consultarPartido_existente_retornaPartido() {
        Partido p = service.crearPartido(torneo.getId(), local.getId(), visitante.getId(), LocalDateTime.now(), "cancha 1");
        assertNotNull(service.consultarPartido(p.getId()));
    }

    @Test
    void consultarPartido_inexistente_lanzaExcepcion() {
        assertThrows(RuntimeException.class, () -> service.consultarPartido("uuid-inexistente"));
    }

    @Test
    void consultarPartidosPorTorneo_retornaLista() {
        service.crearPartido(torneo.getId(), local.getId(), visitante.getId(), LocalDateTime.now(), "cancha 1");
        assertEquals(1, service.consultarPartidosPorTorneo(torneo.getId()).size());
    }

    @Test
    void consultarPartidosPorEquipo_retornaLista() {
        service.crearPartido(torneo.getId(), local.getId(), visitante.getId(), LocalDateTime.now(), "cancha 1");
        assertFalse(service.consultarPartidosPorEquipo(local.getId()).isEmpty());
    }
}
