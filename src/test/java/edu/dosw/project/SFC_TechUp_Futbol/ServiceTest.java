package edu.dosw.project.SFC_TechUp_Futbol;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Equipo;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.Torneo;
import edu.dosw.project.SFC_TechUp_Futbol.core.repository.EquipoRepositoryImpl;
import edu.dosw.project.SFC_TechUp_Futbol.core.repository.TorneoRepositoryImpl;
import edu.dosw.project.SFC_TechUp_Futbol.core.service.EquipoService;
import edu.dosw.project.SFC_TechUp_Futbol.core.service.TorneoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
}
