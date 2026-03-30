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
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ServiceTest {

    private TorneoService torneoService;
    private EquipoService equipoService;

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
        when(torneoRepo.findById(anyString())).thenAnswer(inv -> java.util.Optional.ofNullable(torneoStore.get(inv.<String>getArgument(0))));
        when(torneoRepo.findAll()).thenAnswer(inv -> new ArrayList<>(torneoStore.values()));
        torneoService = new TorneoService(torneoRepo);

        Map<String, Equipo> equipoStore = new HashMap<>();
        EquipoRepository equipoRepo = mock(EquipoRepository.class);
        when(equipoRepo.save(any())).thenAnswer(inv -> {
            Equipo e = inv.getArgument(0);
            if (e.getId() == null) e.setId(UUID.randomUUID().toString());
            equipoStore.put(e.getId(), e);
            return e;
        });
        when(equipoRepo.findById(anyString())).thenAnswer(inv -> java.util.Optional.ofNullable(equipoStore.get(inv.<String>getArgument(0))));
        when(equipoRepo.findAll()).thenAnswer(inv -> new ArrayList<>(equipoStore.values()));
        equipoService = new EquipoService(equipoRepo);
    }

    @Test
    void crearTorneo_seGuardaCorrectamente() {
        Torneo torneo = new Torneo(null, "Copa DOSW", LocalDateTime.now(), LocalDateTime.now().plusDays(5), 8, 50.0);
        torneoService.crear(torneo, new HashMap<>());
        assertFalse(torneoService.listar().isEmpty());
    }

    @Test
    void obtenerTorneo_idExistente_retornaTorneo() {
        Torneo torneo = new Torneo(null, "Copa DOSW", LocalDateTime.now(), LocalDateTime.now().plusDays(5), 8, 50.0);
        torneoService.crear(torneo, new HashMap<>());
        assertNotNull(torneoService.obtener(torneo.getId()));
    }

    @Test
    void obtenerTorneo_idInexistente_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> torneoService.obtener("uuid-inexistente"));
    }

    @Test
    void listarTorneos_despuesDeCrear_retornaUno() {
        Torneo torneo = new Torneo(null, "Copa DOSW", LocalDateTime.now(), LocalDateTime.now().plusDays(5), 8, 50.0);
        torneoService.crear(torneo, new HashMap<>());
        assertFalse(torneoService.listar().isEmpty());
    }

    @Test
    void crearEquipo_seGuardaCorrectamente() {
        Equipo equipo = new Equipo(null, "Los Tigres", "escudo.png", "rojo", "blanco", "uuid-capitan-1");
        equipoService.crear(equipo, new HashMap<>());
        assertFalse(equipoService.listar().isEmpty());
    }

    @Test
    void obtenerEquipo_idExistente_retornaEquipo() {
        Equipo equipo = new Equipo(null, "Los Tigres", "escudo.png", "rojo", "blanco", "uuid-capitan-1");
        equipoService.crear(equipo, new HashMap<>());
        assertNotNull(equipoService.obtener(equipo.getId()));
    }

    @Test
    void obtenerEquipo_idInexistente_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> equipoService.obtener("uuid-inexistente"));
    }

    @Test
    void agregarJugador_equipoExistente_jugadorAgregado() {
        Equipo equipo = new Equipo(null, "Los Tigres", "escudo.png", "rojo", "blanco", "uuid-capitan-1");
        equipoService.crear(equipo, new HashMap<>());
        equipoService.agregarJugador(equipo.getId(), "uuid-jugador-5");
        assertEquals(1, equipoService.obtener(equipo.getId()).getJugadores().size());
    }

    @Test
    void validarComposicion_menosDeSiete_retornaInvalido() {
        Equipo equipo = new Equipo(null, "Pocos", "", "rojo", "blanco", "uuid-capitan-1");
        equipoService.crear(equipo, new HashMap<>());
        equipoService.agregarJugador(equipo.getId(), "uuid-j1");
        equipoService.agregarJugador(equipo.getId(), "uuid-j2");
        var resultado = equipoService.validarComposicion(equipo.getId());
        assertEquals(false, resultado.get("valido"));
    }

    @Test
    void validarComposicion_sieteJugadores_retornaValido() {
        Equipo equipo = new Equipo(null, "Suficientes", "", "rojo", "blanco", "uuid-capitan-1");
        equipoService.crear(equipo, new HashMap<>());
        for (int i = 1; i <= 7; i++) equipoService.agregarJugador(equipo.getId(), "uuid-j" + i);
        var resultado = equipoService.validarComposicion(equipo.getId());
        assertEquals(true, resultado.get("valido"));
    }

    @Test
    void configurarTorneo_guardaCampos() {
        Torneo torneo = new Torneo(null, "Copa Config",
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
        Torneo torneo = new Torneo(null, "Copa CierreInvalido",
                LocalDateTime.of(2025, 10, 1, 10, 0),
                LocalDateTime.of(2025, 10, 30, 18, 0), 8, 50.0);
        torneoService.crear(torneo, new HashMap<>());
        String id = torneo.getId();
        assertThrows(IllegalArgumentException.class, () ->
                torneoService.configurar(id, null, null, null, null,
                        LocalDateTime.of(2025, 10, 5, 0, 0)));
    }
}
