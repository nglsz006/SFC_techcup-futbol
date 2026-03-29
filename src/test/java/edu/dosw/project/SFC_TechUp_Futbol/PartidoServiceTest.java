package edu.dosw.project.SFC_TechUp_Futbol;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.*;
import edu.dosw.project.SFC_TechUp_Futbol.core.repository.*;
import edu.dosw.project.SFC_TechUp_Futbol.core.service.PartidoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PartidoServiceTest {

    private PartidoServiceImpl service;
    private JugadorRepository jugadorRepo;
    private Torneo torneo;
    private Equipo local;
    private Equipo visitante;

    @BeforeEach
    void setUp() {
        Map<String, Torneo> torneoStore = new HashMap<>();
        TorneoRepository torneoRepo = mock(TorneoRepository.class);
        when(torneoRepo.save(any())).thenAnswer(inv -> {
            Torneo t = inv.getArgument(0);
            if (t.getId() == null) t.setId(UUID.randomUUID().toString());
            torneoStore.put(t.getId(), t);
            return t;
        });
        when(torneoRepo.findById(anyString())).thenAnswer(inv -> Optional.ofNullable(torneoStore.get(inv.<String>getArgument(0))));
        when(torneoRepo.findAll()).thenAnswer(inv -> new ArrayList<>(torneoStore.values()));

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

        Map<String, Jugador> jugadorStore = new HashMap<>();
        jugadorRepo = mock(JugadorRepository.class);
        when(jugadorRepo.save(any())).thenAnswer(inv -> {
            Jugador j = inv.getArgument(0);
            if (j.getId() == null) j.setId(UUID.randomUUID().toString());
            jugadorStore.put(j.getId(), j);
            return j;
        });
        when(jugadorRepo.findById(anyString())).thenAnswer(inv -> Optional.ofNullable(jugadorStore.get(inv.<String>getArgument(0))));
        when(jugadorRepo.findAll()).thenAnswer(inv -> new ArrayList<>(jugadorStore.values()));

        Map<String, Partido> partidoStore = new HashMap<>();
        PartidoRepository partidoRepo = mock(PartidoRepository.class);
        when(partidoRepo.save(any())).thenAnswer(inv -> {
            Partido p = inv.getArgument(0);
            if (p.getId() == null) p.setId(UUID.randomUUID().toString());
            partidoStore.put(p.getId(), p);
            return p;
        });
        when(partidoRepo.findById(anyString())).thenAnswer(inv -> Optional.ofNullable(partidoStore.get(inv.<String>getArgument(0))));
        when(partidoRepo.findByTorneoId(anyString())).thenAnswer(inv -> {
            String tid = inv.getArgument(0);
            return partidoStore.values().stream().filter(p -> p.getTorneo() != null && tid.equals(p.getTorneo().getId())).collect(Collectors.toList());
        });
        when(partidoRepo.findByEstado(any())).thenAnswer(inv -> {
            Partido.PartidoEstado estado = inv.getArgument(0);
            return partidoStore.values().stream().filter(p -> p.getEstado() == estado).collect(Collectors.toList());
        });
        when(partidoRepo.findByEquipoLocalIdOrEquipoVisitanteId(anyString(), anyString())).thenAnswer(inv -> {
            String lid = inv.getArgument(0); String vid = inv.getArgument(1);
            return partidoStore.values().stream().filter(p ->
                    (p.getEquipoLocal() != null && lid.equals(p.getEquipoLocal().getId())) ||
                    (p.getEquipoVisitante() != null && vid.equals(p.getEquipoVisitante().getId()))
            ).collect(Collectors.toList());
        });

        service = new PartidoServiceImpl(partidoRepo, torneoRepo, equipoRepo, jugadorRepo);
        torneo = torneoRepo.save(new Torneo(null, "Copa", LocalDateTime.now(), LocalDateTime.now().plusDays(5), 8, 50));
        local = equipoRepo.save(new Equipo(null, "Local", "", "rojo", "blanco", "uuid-cap-1"));
        visitante = equipoRepo.save(new Equipo(null, "Visitante", "", "azul", "negro", "uuid-cap-2"));
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
        jugadorRepo.save(jugador);
        Partido p = service.crearPartido(torneo.getId(), local.getId(), visitante.getId(), LocalDateTime.now(), "cancha 1");
        service.iniciarPartido(p.getId());
        Partido con_gol = service.registrarGoleador(p.getId(), jugador.getId(), 30);
        assertEquals(1, con_gol.getGoles().size());
    }

    @Test
    void registrarGoleador_noEnCurso_lanzaExcepcion() {
        Jugador jugador = new Jugador(null, "Juan", "juan@test.com", "pass", Usuario.TipoUsuario.ESTUDIANTE, 10, Jugador.Posicion.DELANTERO, true, "");
        jugadorRepo.save(jugador);
        Partido p = service.crearPartido(torneo.getId(), local.getId(), visitante.getId(), LocalDateTime.now(), "cancha 1");
        assertThrows(IllegalStateException.class, () -> service.registrarGoleador(p.getId(), jugador.getId(), 30));
    }

    @Test
    void registrarSancion_enCurso_agregaSancionAlPartidoYAlJugador() {
        Jugador jugador = new Jugador(null, "Juan", "juan@test.com", "pass", Usuario.TipoUsuario.ESTUDIANTE, 10, Jugador.Posicion.DELANTERO, true, "");
        jugadorRepo.save(jugador);
        Partido p = service.crearPartido(torneo.getId(), local.getId(), visitante.getId(), LocalDateTime.now(), "cancha 1");
        service.iniciarPartido(p.getId());
        Partido conSancion = service.registrarSancion(p.getId(), jugador.getId(), Sancion.TipoSancion.TARJETA_AMARILLA, "Falta reiterada sobre el rival");
        assertEquals(1, conSancion.getSanciones().size());
        assertEquals(Sancion.TipoSancion.TARJETA_AMARILLA, conSancion.getSanciones().get(0).getTipoSancion());
        assertTrue(jugador.tieneSancion(Sancion.TipoSancion.TARJETA_AMARILLA));
    }

    @Test
    void registrarSancion_noEnCurso_lanzaExcepcion() {
        Jugador jugador = new Jugador(null, "Juan", "juan@test.com", "pass", Usuario.TipoUsuario.ESTUDIANTE, 10, Jugador.Posicion.DELANTERO, true, "");
        jugadorRepo.save(jugador);
        Partido p = service.crearPartido(torneo.getId(), local.getId(), visitante.getId(), LocalDateTime.now(), "cancha 1");
        assertThrows(IllegalStateException.class, () ->
                service.registrarSancion(p.getId(), jugador.getId(), Sancion.TipoSancion.TARJETA_ROJA, "Juego brusco"));
    }

    @Test
    void registrarSancion_agresionVerbal_agregaCorrectamente() {
        Jugador jugador = new Jugador(null, "Juan", "juan@test.com", "pass", Usuario.TipoUsuario.ESTUDIANTE, 10, Jugador.Posicion.DELANTERO, true, "");
        jugadorRepo.save(jugador);
        Partido p = service.crearPartido(torneo.getId(), local.getId(), visitante.getId(), LocalDateTime.now(), "cancha 1");
        service.iniciarPartido(p.getId());
        service.registrarSancion(p.getId(), jugador.getId(), Sancion.TipoSancion.AGRESION_VERBAL, "Insultos al árbitro en el minuto 35");
        assertTrue(jugador.tieneSancion(Sancion.TipoSancion.AGRESION_VERBAL));
        assertEquals(1, jugador.getSancionesPorTipo(Sancion.TipoSancion.AGRESION_VERBAL).size());
    }

    @Test
    void registrarSancion_multiplesSancionesAlMismoJugador_acumula() {
        Jugador jugador = new Jugador(null, "Juan", "juan@test.com", "pass", Usuario.TipoUsuario.ESTUDIANTE, 10, Jugador.Posicion.DELANTERO, true, "");
        jugadorRepo.save(jugador);
        Partido p = service.crearPartido(torneo.getId(), local.getId(), visitante.getId(), LocalDateTime.now(), "cancha 1");
        service.iniciarPartido(p.getId());
        service.registrarSancion(p.getId(), jugador.getId(), Sancion.TipoSancion.TARJETA_AMARILLA, "Primera falta en minuto 20");
        service.registrarSancion(p.getId(), jugador.getId(), Sancion.TipoSancion.TARJETA_AMARILLA, "Segunda falta en minuto 60");
        assertEquals(2, jugador.getSancionesPorTipo(Sancion.TipoSancion.TARJETA_AMARILLA).size());
        assertEquals(2, jugador.getSanciones().size());
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
