package edu.dosw.project.SFC_TechUp_Futbol;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Equipo;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.Torneo;
import edu.dosw.project.SFC_TechUp_Futbol.core.service.EquipoService;
import edu.dosw.project.SFC_TechUp_Futbol.core.service.TorneoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.HashMap;
import static org.junit.jupiter.api.Assertions.*;

class ServiceTest {

    private TorneoService torneoService;
    private EquipoService equipoService;

    @BeforeEach
    void setUp() {
        torneoService = new TorneoService(new TorneoRepositoryImpl());
        equipoService = new EquipoService(new EquipoRepositoryImpl());
    }

    // --- TorneoService ---

    @Test
    void crearTorneo_seGuardaCorrectamente() {
        Torneo torneo = new Torneo(0, "Copa DOSW", LocalDateTime.now(), LocalDateTime.now().plusDays(5), 8, 50.0);
        torneoService.crear(torneo, new HashMap<>());
        assertFalse(torneoService.listar().isEmpty());
    }

    @Test
    void obtenerTorneo_idExistente_retornaTorneo() {
        Torneo torneo = new Torneo(1, "Copa DOSW", LocalDateTime.now(), LocalDateTime.now().plusDays(5), 8, 50.0);
        torneoService.crear(torneo, new HashMap<>());
        assertNotNull(torneoService.obtener(1));
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

    // --- EquipoService ---

    @Test
    void crearEquipo_seGuardaCorrectamente() {
        Equipo equipo = new Equipo(0, "Los Tigres", "escudo.png", "rojo", "blanco", 1);
        equipoService.crear(equipo, new HashMap<>());
        assertFalse(equipoService.listar().isEmpty());
    }

    @Test
    void obtenerEquipo_idExistente_retornaEquipo() {
        Equipo equipo = new Equipo(1, "Los Tigres", "escudo.png", "rojo", "blanco", 1);
        equipoService.crear(equipo, new HashMap<>());
        assertNotNull(equipoService.obtener(1));
    }

    @Test
    void obtenerEquipo_idInexistente_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> equipoService.obtener(99));
    }

    @Test
    void agregarJugador_equipoExistente_jugadorAgregado() {
        Equipo equipo = new Equipo(1, "Los Tigres", "escudo.png", "rojo", "blanco", 1);
        equipoService.crear(equipo, new HashMap<>());
        equipoService.agregarJugador(1, 5);
        assertEquals(1, equipoService.obtener(1).getJugadores().size());
    }

    @Test
    void validarComposicion_menosDeSiete_retornaInvalido() {
        Equipo equipo = new Equipo(2, "Pocos", "", "rojo", "blanco", 1);
        equipoService.crear(equipo, new HashMap<>());
        equipoService.agregarJugador(2, 1);
        equipoService.agregarJugador(2, 2);
        var resultado = equipoService.validarComposicion(2);
        assertEquals(false, resultado.get("valido"));
    }

    @Test
    void validarComposicion_sieteJugadores_retornaValido() {
        Equipo equipo = new Equipo(3, "Suficientes", "", "rojo", "blanco", 1);
        equipoService.crear(equipo, new HashMap<>());
        for (int i = 1; i <= 7; i++) equipoService.agregarJugador(3, i);
        var resultado = equipoService.validarComposicion(3);
        assertEquals(true, resultado.get("valido"));
    }

    @Test
    void configurarTorneo_guardaCampos() {
        Torneo torneo = new Torneo(10, "Copa Config",
                LocalDateTime.of(2025, 10, 1, 10, 0),
                LocalDateTime.of(2025, 10, 30, 18, 0), 8, 50.0);
        torneoService.crear(torneo, new HashMap<>());
        Torneo configurado = torneoService.configurar(10, "Reglamento X", "Cancha A", "Sabados 10am", "Tarjeta roja = 1 fecha",
                LocalDateTime.of(2025, 9, 25, 0, 0));
        assertEquals("Reglamento X", configurado.getReglamento());
        assertEquals("Cancha A", configurado.getCanchas());
    }

    @Test
    void configurarTorneo_cierreInscripcionesInvalido_lanzaExcepcion() {
        Torneo torneo = new Torneo(11, "Copa CierreInvalido",
                LocalDateTime.of(2025, 10, 1, 10, 0),
                LocalDateTime.of(2025, 10, 30, 18, 0), 8, 50.0);
        torneoService.crear(torneo, new HashMap<>());
        assertThrows(IllegalArgumentException.class, () ->
                torneoService.configurar(11, null, null, null, null,
                        LocalDateTime.of(2025, 10, 5, 0, 0)));
    }
}
