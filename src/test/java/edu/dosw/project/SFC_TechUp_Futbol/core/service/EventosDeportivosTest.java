package edu.dosw.project.SFC_TechUp_Futbol.core.service;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.*;
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

class EventosDeportivosTest {

    private PartidoServiceImpl service;
    private JugadorJpaRepository jugadorRepo;
    private JugadorMapper jugadorMapper;
    private Torneo torneo;
    private Equipo local;
    private Equipo visitante;
    private Jugador jugador;

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
        when(partidoRepo.findByTorneoIdAndFase(anyString(), any())).thenReturn(new ArrayList<>());

        service = new PartidoServiceImpl(partidoRepo, partidoMapper, torneoRepo, torneoMapper, equipoRepo, equipoMapper, jugadorRepo, jugadorMapper);

        TorneoEntity te = torneoRepo.save(torneoMapper.toEntity(new Torneo(null, "Copa Tecnica", LocalDateTime.now(), LocalDateTime.now().plusDays(10), 8, 50)));
        torneo = torneoMapper.toDomain(te);
        EquipoEntity le = equipoRepo.save(equipoMapper.toEntity(new Equipo(null, "Los Tigres", "", "rojo", "blanco", "cap-1")));
        local = equipoMapper.toDomain(le);
        EquipoEntity ve = equipoRepo.save(equipoMapper.toEntity(new Equipo(null, "Los Leones", "", "azul", "negro", "cap-2")));
        visitante = equipoMapper.toDomain(ve);

        Jugador j = new Jugador(null, "Carlos", "carlos@test.com", "pass", Usuario.TipoUsuario.ESTUDIANTE, 9, Jugador.Posicion.DELANTERO, true, local.getId());
        JugadorEntity je = jugadorRepo.save(jugadorMapper.toEntity(j));
        j.setId(je.getId());
        jugador = j;
    }

    private Partido crearEIniciar() {
        Partido p = service.crearPartido(torneo.getId(), local.getId(), visitante.getId(), LocalDateTime.now(), "cancha 1");
        service.iniciarPartido(p.getId());
        return p;
    }

    // --- Registro de goles ---

    @Test
    void registrarGol_enCurso_quedaAsociadoAlPartidoYJugador() {
        Partido p = crearEIniciar();
        Partido conGol = service.registrarGoleador(p.getId(), jugador.getId(), 30);
        assertEquals(1, conGol.getGoles().size());
        assertNotNull(conGol.getGoles().get(0).getId());
        assertEquals(30, conGol.getGoles().get(0).getMinuto());
    }

    @Test
    void registrarGol_jugadorInexistente_lanzaExcepcion() {
        Partido p = crearEIniciar();
        assertThrows(RuntimeException.class, () -> service.registrarGoleador(p.getId(), "jugador-inexistente", 30));
    }

    @Test
    void registrarGol_partidoInexistente_lanzaExcepcion() {
        assertThrows(RuntimeException.class, () -> service.registrarGoleador("partido-inexistente", jugador.getId(), 30));
    }

    @Test
    void registrarGol_partidoNoProgramado_lanzaExcepcion() {
        Partido p = service.crearPartido(torneo.getId(), local.getId(), visitante.getId(), LocalDateTime.now(), "cancha 1");
        assertThrows(IllegalStateException.class, () -> service.registrarGoleador(p.getId(), jugador.getId(), 30));
    }

    @Test
    void registrarGol_informacionIncompleta_jugadorIdVacio_lanzaExcepcion() {
        Partido p = crearEIniciar();
        assertThrows(IllegalArgumentException.class, () -> service.registrarGoleador(p.getId(), "", 30));
    }

    // --- Validacion de minuto ---

    @Test
    void registrarGol_minutoFueraDeRango_lanzaExcepcion() {
        Partido p = crearEIniciar();
        assertThrows(IllegalArgumentException.class, () -> service.registrarGoleador(p.getId(), jugador.getId(), 0));
    }

    @Test
    void registrarGol_minutoMaximo_lanzaExcepcion() {
        Partido p = crearEIniciar();
        assertThrows(IllegalArgumentException.class, () -> service.registrarGoleador(p.getId(), jugador.getId(), 121));
    }

    @Test
    void registrarTarjeta_minutoFueraDeRango_lanzaExcepcion() {
        Partido p = crearEIniciar();
        assertThrows(IllegalArgumentException.class, () -> service.registrarTarjeta(p.getId(), jugador.getId(), Partido.Tarjeta.TipoTarjeta.AMARILLA, 0));
    }

    @Test
    void registrarTarjeta_minutoValido_seRegistraCorrectamente() {
        Partido p = crearEIniciar();
        Partido conTarjeta = service.registrarTarjeta(p.getId(), jugador.getId(), Partido.Tarjeta.TipoTarjeta.AMARILLA, 45);
        assertEquals(1, conTarjeta.getTarjetas().size());
        assertEquals(45, conTarjeta.getTarjetas().get(0).getMinuto());
    }

    // --- Registro de tarjetas y sanciones ---

    @Test
    void registrarTarjeta_amarilla_quedaAsociadaAlPartidoYJugador() {
        Partido p = crearEIniciar();
        Partido conTarjeta = service.registrarTarjeta(p.getId(), jugador.getId(), Partido.Tarjeta.TipoTarjeta.AMARILLA, 20);
        assertEquals(1, conTarjeta.getTarjetas().size());
        assertEquals(Partido.Tarjeta.TipoTarjeta.AMARILLA, conTarjeta.getTarjetas().get(0).getTipo());
    }

    @Test
    void registrarTarjeta_roja_quedaAsociadaAlPartido() {
        Partido p = crearEIniciar();
        Partido conTarjeta = service.registrarTarjeta(p.getId(), jugador.getId(), Partido.Tarjeta.TipoTarjeta.ROJA, 60);
        assertEquals(Partido.Tarjeta.TipoTarjeta.ROJA, conTarjeta.getTarjetas().get(0).getTipo());
    }

    @Test
    void registrarTarjeta_tipoNulo_lanzaExcepcion() {
        Partido p = crearEIniciar();
        assertThrows(IllegalArgumentException.class, () -> service.registrarTarjeta(p.getId(), jugador.getId(), null, 20));
    }

    @Test
    void registrarTarjeta_jugadorInexistente_lanzaExcepcion() {
        Partido p = crearEIniciar();
        assertThrows(RuntimeException.class, () -> service.registrarTarjeta(p.getId(), "jugador-inexistente", Partido.Tarjeta.TipoTarjeta.AMARILLA, 20));
    }

    @Test
    void registrarTarjeta_partidoNoEnCurso_lanzaExcepcion() {
        Partido p = service.crearPartido(torneo.getId(), local.getId(), visitante.getId(), LocalDateTime.now(), "cancha 1");
        assertThrows(IllegalStateException.class, () -> service.registrarTarjeta(p.getId(), jugador.getId(), Partido.Tarjeta.TipoTarjeta.AMARILLA, 20));
    }

    @Test
    void registrarSancion_enCurso_quedaAsociadaAlPartidoYJugador() {
        Partido p = crearEIniciar();
        Partido conSancion = service.registrarSancion(p.getId(), jugador.getId(), Sancion.TipoSancion.AGRESION_VERBAL, "Insultos al arbitro");
        assertEquals(1, conSancion.getSanciones().size());
        assertEquals(Sancion.TipoSancion.AGRESION_VERBAL, conSancion.getSanciones().get(0).getTipoSancion());
    }

    @Test
    void registrarSancion_descripcionVacia_lanzaExcepcion() {
        Partido p = crearEIniciar();
        assertThrows(IllegalArgumentException.class, () -> service.registrarSancion(p.getId(), jugador.getId(), Sancion.TipoSancion.TARJETA_ROJA, ""));
    }

    @Test
    void registrarSancion_jugadorInexistente_lanzaExcepcion() {
        Partido p = crearEIniciar();
        assertThrows(RuntimeException.class, () -> service.registrarSancion(p.getId(), "jugador-inexistente", Sancion.TipoSancion.TARJETA_AMARILLA, "Falta"));
    }

    @Test
    void registrarSancion_partidoNoEnCurso_lanzaExcepcion() {
        Partido p = service.crearPartido(torneo.getId(), local.getId(), visitante.getId(), LocalDateTime.now(), "cancha 1");
        assertThrows(IllegalStateException.class, () -> service.registrarSancion(p.getId(), jugador.getId(), Sancion.TipoSancion.TARJETA_AMARILLA, "Falta"));
    }

    // --- Goles y marcador ---

    @Test
    void registrarGol_jugadorDelEquipoLocal_incrementaMarcadorLocal() {
        jugador.setEquipo(local.getId());
        JugadorEntity je = jugadorRepo.save(jugadorMapper.toEntity(jugador));
        jugador.setId(je.getId());

        Partido p = crearEIniciar();
        Partido conGol = service.registrarGoleador(p.getId(), jugador.getId(), 10);
        assertEquals(1, conGol.getMarcadorLocal());
        assertEquals(0, conGol.getMarcadorVisitante());
    }

    @Test
    void registrarGol_jugadorSinEquipo_incrementaMarcadorVisitante() {
        Partido p = crearEIniciar();
        Partido conGol = service.registrarGoleador(p.getId(), jugador.getId(), 10);
        assertEquals(0, conGol.getMarcadorLocal());
        assertEquals(1, conGol.getMarcadorVisitante());
    }

    @Test
    void registrarResultado_actualizaMarcadorCorrectamente() {
        Partido p = crearEIniciar();
        Partido conResultado = service.registrarResultado(p.getId(), 2, 1);
        assertEquals(2, conResultado.getMarcadorLocal());
        assertEquals(1, conResultado.getMarcadorVisitante());
    }

    @Test
    void registrarResultado_marcadorNegativo_lanzaExcepcion() {
        Partido p = crearEIniciar();
        assertThrows(IllegalArgumentException.class, () -> service.registrarResultado(p.getId(), -1, 0));
    }

    // --- Resultado final ---

    @Test
    void finalizarPartido_conResultado_conservaMarcador() {
        Partido p = crearEIniciar();
        service.registrarResultado(p.getId(), 3, 2);
        Partido finalizado = service.finalizarPartido(p.getId());
        assertEquals(Partido.PartidoEstado.FINALIZADO, finalizado.getEstado());
        assertEquals(3, finalizado.getMarcadorLocal());
        assertEquals(2, finalizado.getMarcadorVisitante());
    }

    @Test
    void finalizarPartido_conGoles_conservaEventos() {
        Partido p = crearEIniciar();
        service.registrarGoleador(p.getId(), jugador.getId(), 15);
        Partido finalizado = service.finalizarPartido(p.getId());
        assertEquals(Partido.PartidoEstado.FINALIZADO, finalizado.getEstado());
        assertEquals(1, finalizado.getGoles().size());
    }

    // --- Consulta de eventos ---

    @Test
    void consultarEventos_partidoConEventos_retornaGolesTarjetasYSanciones() {
        Partido p = crearEIniciar();
        service.registrarGoleador(p.getId(), jugador.getId(), 10);
        service.registrarTarjeta(p.getId(), jugador.getId(), Partido.Tarjeta.TipoTarjeta.AMARILLA, 25);
        service.registrarSancion(p.getId(), jugador.getId(), Sancion.TipoSancion.AGRESION_VERBAL, "Insultos");

        Map<String, Object> eventos = service.consultarEventos(p.getId());
        assertNotNull(eventos.get("goles"));
        assertNotNull(eventos.get("tarjetas"));
        assertNotNull(eventos.get("sanciones"));
    }

    @Test
    void consultarEventos_partidoSinEventos_retornaListasVacias() {
        Partido p = crearEIniciar();
        Map<String, Object> eventos = service.consultarEventos(p.getId());
        assertTrue(((List<?>) eventos.get("goles")).isEmpty());
        assertTrue(((List<?>) eventos.get("tarjetas")).isEmpty());
        assertTrue(((List<?>) eventos.get("sanciones")).isEmpty());
    }

    @Test
    void consultarEventos_partidoInexistente_lanzaExcepcion() {
        assertThrows(RuntimeException.class, () -> service.consultarEventos("partido-inexistente"));
    }

    // --- Integridad funcional ---

    @Test
    void flujoCompleto_eventos_marcadorYResultadoCoherentes() {
        jugador.setEquipo(local.getId());
        JugadorEntity je = jugadorRepo.save(jugadorMapper.toEntity(jugador));
        jugador.setId(je.getId());

        Partido p = crearEIniciar();
        service.registrarGoleador(p.getId(), jugador.getId(), 10);
        service.registrarGoleador(p.getId(), jugador.getId(), 55);
        service.registrarTarjeta(p.getId(), jugador.getId(), Partido.Tarjeta.TipoTarjeta.AMARILLA, 30);
        service.registrarSancion(p.getId(), jugador.getId(), Sancion.TipoSancion.TARJETA_AMARILLA, "Falta reiterada");

        Partido finalizado = service.finalizarPartido(p.getId());
        assertEquals(Partido.PartidoEstado.FINALIZADO, finalizado.getEstado());
        assertEquals(2, finalizado.getGoles().size());
        assertEquals(2, finalizado.getMarcadorLocal());

        Map<String, Object> eventos = service.consultarEventos(finalizado.getId());
        assertEquals(2, ((List<?>) eventos.get("goles")).size());
        assertEquals(1, ((List<?>) eventos.get("tarjetas")).size());
        assertEquals(1, ((List<?>) eventos.get("sanciones")).size());
    }
}
