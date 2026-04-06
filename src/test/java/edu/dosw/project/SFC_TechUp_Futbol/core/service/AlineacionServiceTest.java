package edu.dosw.project.SFC_TechUp_Futbol.core.service;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Alineacion;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.Equipo;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.Partido;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.Torneo;
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

class AlineacionServiceTest {

    private AlineacionService service;
    private EquipoJpaRepository equipoRepo;
    private PartidoJpaRepository partidoRepo;
    private EquipoMapper equipoMapper;
    private TorneoMapper torneoMapper;
    private PartidoMapper partidoMapper;
    private Equipo equipo;
    private Partido partido;

    private static final List<String> ONCE_JUGADORES = List.of(
            "j1", "j2", "j3", "j4", "j5", "j6", "j7", "j8", "j9", "j10", "j11"
    );

    @BeforeEach
    void setUp() {
        AlineacionMapper alineacionMapper = new AlineacionMapper();
        equipoMapper = new EquipoMapper();
        torneoMapper = new TorneoMapper();
        JugadorMapper jugadorMapper = new JugadorMapper();
        partidoMapper = new PartidoMapper(torneoMapper, equipoMapper, jugadorMapper);

        Map<String, AlineacionEntity> alineacionStore = new HashMap<>();
        AlineacionJpaRepository alineacionRepo = mock(AlineacionJpaRepository.class);
        when(alineacionRepo.save(any())).thenAnswer(inv -> {
            AlineacionEntity e = inv.getArgument(0);
            if (e.getId() == null) e.setId(UUID.randomUUID().toString());
            alineacionStore.put(e.getId(), e);
            return e;
        });
        when(alineacionRepo.findById(anyString())).thenAnswer(inv -> Optional.ofNullable(alineacionStore.get(inv.<String>getArgument(0))));
        when(alineacionRepo.findAll()).thenAnswer(inv -> new ArrayList<>(alineacionStore.values()));
        when(alineacionRepo.findByPartidoIdAndEquipoId(anyString(), anyString())).thenAnswer(inv -> {
            String pid = inv.getArgument(0); String eid = inv.getArgument(1);
            return alineacionStore.values().stream().filter(e -> pid.equals(e.getPartidoId()) && eid.equals(e.getEquipoId())).findFirst();
        });
        when(alineacionRepo.findByPartidoId(anyString())).thenAnswer(inv -> {
            String pid = inv.getArgument(0);
            return alineacionStore.values().stream().filter(e -> pid.equals(e.getPartidoId())).collect(Collectors.toList());
        });

        Map<String, EquipoEntity> equipoStore = new HashMap<>();
        equipoRepo = mock(EquipoJpaRepository.class);
        when(equipoRepo.save(any())).thenAnswer(inv -> {
            EquipoEntity e = inv.getArgument(0);
            if (e.getId() == null) e.setId(UUID.randomUUID().toString());
            equipoStore.put(e.getId(), e);
            return e;
        });
        when(equipoRepo.findById(anyString())).thenAnswer(inv -> Optional.ofNullable(equipoStore.get(inv.<String>getArgument(0))));

        Map<String, PartidoEntity> partidoStore = new HashMap<>();
        partidoRepo = mock(PartidoJpaRepository.class);
        when(partidoRepo.save(any())).thenAnswer(inv -> {
            PartidoEntity e = inv.getArgument(0);
            if (e.getId() == null) e.setId(UUID.randomUUID().toString());
            partidoStore.put(e.getId(), e);
            return e;
        });
        when(partidoRepo.findById(anyString())).thenAnswer(inv -> Optional.ofNullable(partidoStore.get(inv.<String>getArgument(0))));

        Map<String, TorneoEntity> torneoStore = new HashMap<>();
        TorneoJpaRepository torneoRepo = mock(TorneoJpaRepository.class);
        when(torneoRepo.save(any())).thenAnswer(inv -> {
            TorneoEntity e = inv.getArgument(0);
            if (e.getId() == null) e.setId(UUID.randomUUID().toString());
            torneoStore.put(e.getId(), e);
            return e;
        });
        when(torneoRepo.findById(anyString())).thenAnswer(inv -> Optional.ofNullable(torneoStore.get(inv.<String>getArgument(0))));

        service = new AlineacionService(alineacionRepo, alineacionMapper, equipoRepo, equipoMapper, partidoRepo, partidoMapper);

        Equipo eq = new Equipo(null, "Los Tigres", "", "rojo", "blanco", "cap-1");
        eq.setJugadores(new ArrayList<>(ONCE_JUGADORES));
        EquipoEntity savedEquipo = equipoRepo.save(equipoMapper.toEntity(eq));
        equipo = equipoMapper.toDomain(savedEquipo);

        Torneo torneo = new Torneo(null, "Copa", LocalDateTime.now(), LocalDateTime.now().plusDays(5), 8, 50);
        TorneoEntity savedTorneo = torneoRepo.save(torneoMapper.toEntity(torneo));

        Equipo visitante = new Equipo(null, "Los Leones", "", "azul", "negro", "cap-2");
        EquipoEntity savedVisitante = equipoRepo.save(equipoMapper.toEntity(visitante));

        Partido p = new Partido();
        p.setId(UUID.randomUUID().toString());
        p.setTorneo(torneoMapper.toDomain(savedTorneo));
        p.setEquipoLocal(equipo);
        p.setEquipoVisitante(equipoMapper.toDomain(savedVisitante));
        p.setFecha(LocalDateTime.now());
        p.setCancha("Cancha 1");
        PartidoEntity savedPartido = partidoRepo.save(partidoMapper.toEntity(p));
        partido = partidoMapper.toDomain(savedPartido);
    }

    @Test
    void registrar_datosValidos_retornaAlineacion() {
        Alineacion a = service.registrar(equipo.getId(), partido.getId(),
                Alineacion.Formacion.F_4_4_2, ONCE_JUGADORES, List.of());
        assertNotNull(a.getId());
        assertEquals(equipo.getId(), a.getEquipoId());
        assertEquals(partido.getId(), a.getPartidoId());
        assertEquals(Alineacion.Formacion.F_4_4_2, a.getFormacion());
        assertEquals(11, a.getTitulares().size());
    }

    @Test
    void registrar_equipoInexistente_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () ->
                service.registrar("equipo-inexistente", partido.getId(),
                        Alineacion.Formacion.F_4_3_3, ONCE_JUGADORES, List.of()));
    }

    @Test
    void registrar_partidoInexistente_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () ->
                service.registrar(equipo.getId(), "partido-inexistente",
                        Alineacion.Formacion.F_4_3_3, ONCE_JUGADORES, List.of()));
    }

    @Test
    void registrar_formacionNula_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () ->
                service.registrar(equipo.getId(), partido.getId(), null, ONCE_JUGADORES, List.of()));
    }

    @Test
    void registrar_menosDe11Titulares_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () ->
                service.registrar(equipo.getId(), partido.getId(),
                        Alineacion.Formacion.F_4_4_2, List.of("j1", "j2", "j3"), List.of()));
    }

    @Test
    void registrar_masde7Reservas_lanzaExcepcion() {
        List<String> muchasReservas = List.of("r1", "r2", "r3", "r4", "r5", "r6", "r7", "r8");
        assertThrows(IllegalArgumentException.class, () ->
                service.registrar(equipo.getId(), partido.getId(),
                        Alineacion.Formacion.F_4_4_2, ONCE_JUGADORES, muchasReservas));
    }

    @Test
    void registrar_jugadorFueraDelEquipo_lanzaExcepcion() {
        List<String> titularesConAjeno = new ArrayList<>(ONCE_JUGADORES.subList(0, 10));
        titularesConAjeno.add("jugador-ajeno");
        assertThrows(IllegalArgumentException.class, () ->
                service.registrar(equipo.getId(), partido.getId(),
                        Alineacion.Formacion.F_4_4_2, titularesConAjeno, List.of()));
    }

    @Test
    void obtener_existente_retornaAlineacion() {
        Alineacion a = service.registrar(equipo.getId(), partido.getId(),
                Alineacion.Formacion.F_3_5_2, ONCE_JUGADORES, List.of());
        Alineacion resultado = service.obtener(a.getId());
        assertNotNull(resultado);
        assertEquals(Alineacion.Formacion.F_3_5_2, resultado.getFormacion());
    }

    @Test
    void obtener_inexistente_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> service.obtener("id-inexistente"));
    }

    @Test
    void obtenerPorPartidoYEquipo_retornaAlineacion() {
        service.registrar(equipo.getId(), partido.getId(),
                Alineacion.Formacion.F_4_5_1, ONCE_JUGADORES, List.of());
        Alineacion resultado = service.obtenerPorPartidoYEquipo(partido.getId(), equipo.getId());
        assertNotNull(resultado);
        assertEquals(equipo.getId(), resultado.getEquipoId());
    }

    @Test
    void obtenerRival_conDosAlineaciones_retornaLaDelRival() {
        Equipo rival = equipoMapper.toDomain(equipoRepo.save(equipoMapper.toEntity(
                new Equipo(null, "Rival", "", "verde", "blanco", "cap-3"))));

        service.registrar(equipo.getId(), partido.getId(),
                Alineacion.Formacion.F_4_4_2, ONCE_JUGADORES, List.of());
        service.registrar(rival.getId(), partido.getId(),
                Alineacion.Formacion.F_5_3_2, ONCE_JUGADORES, List.of());

        Alineacion rivalAlineacion = service.obtenerRival(partido.getId(), equipo.getId());
        assertEquals(rival.getId(), rivalAlineacion.getEquipoId());
    }

    @Test
    void obtenerRival_sinAlineacionRival_lanzaExcepcion() {
        service.registrar(equipo.getId(), partido.getId(),
                Alineacion.Formacion.F_4_4_2, ONCE_JUGADORES, List.of());
        assertThrows(IllegalArgumentException.class, () ->
                service.obtenerRival(partido.getId(), equipo.getId()));
    }

    @Test
    void listar_retornaTodasLasAlineaciones() {
        service.registrar(equipo.getId(), partido.getId(),
                Alineacion.Formacion.F_4_4_2, ONCE_JUGADORES, List.of());
        assertEquals(1, service.listar().size());
    }

    @Test
    void registrar_conReservas_persisteCorrectamente() {
        List<String> reservas = List.of("j1", "j2", "j3");
        Alineacion a = service.registrar(equipo.getId(), partido.getId(),
                Alineacion.Formacion.F_4_3_3, ONCE_JUGADORES, reservas);
        assertEquals(3, a.getReservas().size());
    }
}
