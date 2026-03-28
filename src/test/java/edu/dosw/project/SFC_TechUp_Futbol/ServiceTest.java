package edu.dosw.project.SFC_TechUp_Futbol;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Equipo;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.Torneo;
import edu.dosw.project.SFC_TechUp_Futbol.core.repository.EquipoRepository;
import edu.dosw.project.SFC_TechUp_Futbol.core.repository.TorneoRepository;
import edu.dosw.project.SFC_TechUp_Futbol.core.service.EquipoService;
import edu.dosw.project.SFC_TechUp_Futbol.core.service.TorneoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ServiceTest {

    private TorneoService torneoService;
    private EquipoService equipoService;

    @BeforeEach
    void setUp() {
        Map<Integer, Torneo> torneoStore = new HashMap<>();
        AtomicInteger torneoIdGen = new AtomicInteger(1);
        TorneoRepository torneoRepo = mock(TorneoRepository.class);
        when(torneoRepo.save(any())).thenAnswer(inv -> {
            Torneo t = inv.getArgument(0);
            if (t.getId() == 0) t.setId(torneoIdGen.getAndIncrement());
            torneoStore.put(t.getId(), t);
            return t;
        });
        when(torneoRepo.findById(anyInt())).thenAnswer(inv -> java.util.Optional.ofNullable(torneoStore.get(inv.<Integer>getArgument(0))));
        when(torneoRepo.findAll()).thenAnswer(inv -> new ArrayList<>(torneoStore.values()));
        torneoService = new TorneoService(torneoRepo);

        Map<Integer, Equipo> equipoStore = new HashMap<>();
        AtomicInteger equipoIdGen = new AtomicInteger(1);
        EquipoRepository equipoRepo = mock(EquipoRepository.class);
        when(equipoRepo.save(any())).thenAnswer(inv -> {
            Equipo e = inv.getArgument(0);
            if (e.getId() == 0) e.setId(equipoIdGen.getAndIncrement());
            equipoStore.put(e.getId(), e);
            return e;
        });
        when(equipoRepo.findById(anyInt())).thenAnswer(inv -> java.util.Optional.ofNullable(equipoStore.get(inv.<Integer>getArgument(0))));
        when(equipoRepo.findAll()).thenAnswer(inv -> new ArrayList<>(equipoStore.values()));
        equipoService = new EquipoService(equipoRepo);
    }

    @Test
    void crearTorneo_seGuardaCorrectamente() {
        Torneo torneo = new Torneo(0, "Copa DOSW", LocalDateTime.now(), LocalDateTime.now().plusDays(5), 8, 50.0);
        torneoService.crear(torneo, new HashMap<>());
        assertFalse(torneoService.listar().isEmpty());
    }

    @Test
    void obtenerTorneo_idExistente_retornaTorneo() {
        Torneo torneo = new Torneo(0, "Copa DOSW", LocalDateTime.now(), LocalDateTime.now().plusDays(5), 8, 50.0);
        torneoService.crear(torneo, new HashMap<>());
        assertNotNull(torneoService.obtener(torneo.getId()));
    }

    @Test
    void obtenerTorneo_idInexistente_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> torneoService.obtener(99));
    }

    @Test
    void listarTorneos_despuesDeCrear_retornaUno() {
        Torneo torneo = new Torneo(0, "Copa DOSW", LocalDateTime.now(), LocalDateTime.now().plusDays(5), 8, 50.0);
        torneoService.crear(torneo, new HashMap<>());
        assertFalse(torneoService.listar().isEmpty());
    }

    @Test
    void crearEquipo_seGuardaCorrectamente() {
        Equipo equipo = new Equipo(0, "Los Tigres", "escudo.png", "rojo", "blanco", 1);
        equipoService.crear(equipo, new HashMap<>());
        assertFalse(equipoService.listar().isEmpty());
    }

    @Test
    void obtenerEquipo_idExistente_retornaEquipo() {
        Equipo equipo = new Equipo(0, "Los Tigres", "escudo.png", "rojo", "blanco", 1);
        equipoService.crear(equipo, new HashMap<>());
        assertNotNull(equipoService.obtener(equipo.getId()));
    }

    @Test
    void obtenerEquipo_idInexistente_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> equipoService.obtener(99));
    }

    @Test
    void agregarJugador_equipoExistente_jugadorAgregado() {
        Equipo equipo = new Equipo(0, "Los Tigres", "escudo.png", "rojo", "blanco", 1);
        equipoService.crear(equipo, new HashMap<>());
        equipoService.agregarJugador(equipo.getId(), 5);
        assertEquals(1, equipoService.obtener(equipo.getId()).getJugadores().size());
    }

    @Test
    void validarComposicion_menosDeSiete_retornaInvalido() {
        Equipo equipo = new Equipo(0, "Pocos", "", "rojo", "blanco", 1);
        equipoService.crear(equipo, new HashMap<>());
        equipoService.agregarJugador(equipo.getId(), 1);
        equipoService.agregarJugador(equipo.getId(), 2);
        var resultado = equipoService.validarComposicion(equipo.getId());
        assertEquals(false, resultado.get("valido"));
    }

    @Test
    void validarComposicion_sieteJugadores_retornaValido() {
        Equipo equipo = new Equipo(0, "Suficientes", "", "rojo", "blanco", 1);
        equipoService.crear(equipo, new HashMap<>());
        for (int i = 1; i <= 7; i++) equipoService.agregarJugador(equipo.getId(), i);
        var resultado = equipoService.validarComposicion(equipo.getId());
        assertEquals(true, resultado.get("valido"));
    }

    @Test
    void configurarTorneo_guardaCampos() {
        Torneo torneo = new Torneo(0, "Copa Config",
                LocalDateTime.of(2025, 10, 1, 10, 0),
                LocalDateTime.of(2025, 10, 30, 18, 0), 8, 50.0);
        torneoService.crear(torneo, new HashMap<>());
        Torneo configurado = torneoService.configurar(torneo.getId(), "Reglamento X", "Cancha A", "Sabados 10am", "Tarjeta roja = 1 fecha",
                LocalDateTime.of(2025, 9, 25, 0, 0));
        assertEquals("Reglamento X", configurado.getReglamento());
        assertEquals("Cancha A", configurado.getCanchas());
    }

    @Test
    void configurarTorneo_cierreInscripcionesInvalido_lanzaExcepcion() {
        Torneo torneo = new Torneo(0, "Copa CierreInvalido",
                LocalDateTime.of(2025, 10, 1, 10, 0),
                LocalDateTime.of(2025, 10, 30, 18, 0), 8, 50.0);
        torneoService.crear(torneo, new HashMap<>());
        int id = torneo.getId();
        assertThrows(IllegalArgumentException.class, () ->
                torneoService.configurar(id, null, null, null, null,
                        LocalDateTime.of(2025, 10, 5, 0, 0)));
    }
}
