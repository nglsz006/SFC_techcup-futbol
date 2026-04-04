package edu.dosw.project.SFC_TechUp_Futbol.core.service;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Equipo;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.Jugador;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.Usuario;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.entity.EquipoEntity;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.entity.JugadorEntity;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.mapper.EquipoMapper;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.mapper.JugadorMapper;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.repository.EquipoJpaRepository;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.repository.JugadorJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EquipoJugadorServiceTest {

    private EquipoService equipoService;
    private JugadorService jugadorService;

    @BeforeEach
    void setUp() {
        EquipoMapper equipoMapper = new EquipoMapper();
        JugadorMapper jugadorMapper = new JugadorMapper();

        Map<String, EquipoEntity> equipoStore = new HashMap<>();
        EquipoJpaRepository equipoRepo = mock(EquipoJpaRepository.class);
        when(equipoRepo.save(any())).thenAnswer(inv -> {
            EquipoEntity e = inv.getArgument(0);
            if (e.getId() == null) e.setId(UUID.randomUUID().toString());
            equipoStore.put(e.getId(), e);
            return e;
        });
        when(equipoRepo.findById(anyString())).thenAnswer(inv -> Optional.ofNullable(equipoStore.get(inv.<String>getArgument(0))));
        when(equipoRepo.findAll()).thenAnswer(inv -> new ArrayList<>(equipoStore.values()));
        equipoService = new EquipoService(equipoRepo, equipoMapper);

        Map<String, JugadorEntity> jugadorStore = new HashMap<>();
        JugadorJpaRepository jugadorRepo = mock(JugadorJpaRepository.class);
        when(jugadorRepo.save(any())).thenAnswer(inv -> {
            JugadorEntity e = inv.getArgument(0);
            if (e.getId() == null) e.setId(UUID.randomUUID().toString());
            jugadorStore.put(e.getId(), e);
            return e;
        });
        when(jugadorRepo.findById(anyString())).thenAnswer(inv -> Optional.ofNullable(jugadorStore.get(inv.<String>getArgument(0))));
        when(jugadorRepo.findAll()).thenAnswer(inv -> new ArrayList<>(jugadorStore.values()));
        jugadorService = new JugadorService(jugadorRepo, jugadorMapper);
    }

    @Test
    void obtenerEquipo_existente_retornaEquipo() {
        Equipo equipo = new Equipo(null, "Los Tigres", "escudo.png", "rojo", "blanco", "uuid-capitan-1");
        equipoService.crear(equipo, new HashMap<>());
        Equipo resultado = equipoService.obtener(equipo.getId());
        assertNotNull(resultado);
        assertEquals("Los Tigres", resultado.getNombre());
    }

    @Test
    void obtenerEquipo_inexistente_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> equipoService.obtener("id-que-no-existe"));
    }

    @Test
    void listarEquipos_conDosEquipos_retornaAmbos() {
        equipoService.crear(new Equipo(null, "Equipo A", "", "azul", "blanco", "cap-1"), new HashMap<>());
        equipoService.crear(new Equipo(null, "Equipo B", "", "verde", "negro", "cap-2"), new HashMap<>());
        assertEquals(2, equipoService.listar().size());
    }

    @Test
    void listarEquipos_sinEquipos_retornaListaVacia() {
        assertTrue(equipoService.listar().isEmpty());
    }

    @Test
    void agregarJugador_jugadorExistente_seAsociaAlEquipo() {
        Equipo equipo = new Equipo(null, "Los Leones", "", "amarillo", "negro", "cap-1");
        equipoService.crear(equipo, new HashMap<>());

        Jugador jugador = new Jugador(null, "Carlos", "carlos@test.com", "pass",
                Usuario.TipoUsuario.ESTUDIANTE, 9, Jugador.Posicion.DELANTERO, true, "");
        jugadorService.save(jugador);

        equipoService.agregarJugador(equipo.getId(), jugador.getId());

        Equipo actualizado = equipoService.obtener(equipo.getId());
        assertTrue(actualizado.getJugadores().contains(jugador.getId()));
    }

    @Test
    void agregarJugador_equipoInexistente_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () ->
                equipoService.agregarJugador("equipo-inexistente", "jugador-cualquiera"));
    }

    @Test
    void agregarJugador_jugadorInexistente_retornaNull() {
        Jugador resultado = jugadorService.buscarJugadorPorId("jugador-inexistente");
        assertNull(resultado);
    }

    @Test
    void agregarVariosJugadores_equipoTieneConteoCorrect() {
        Equipo equipo = new Equipo(null, "Los Pumas", "", "rojo", "blanco", "cap-3");
        equipoService.crear(equipo, new HashMap<>());

        for (int i = 1; i <= 5; i++) {
            Jugador j = new Jugador(null, "Jugador " + i, "j" + i + "@test.com", "pass",
                    Usuario.TipoUsuario.ESTUDIANTE, i, Jugador.Posicion.DEFENSA, true, "");
            jugadorService.save(j);
            equipoService.agregarJugador(equipo.getId(), j.getId());
        }

        assertEquals(5, equipoService.obtener(equipo.getId()).getJugadores().size());
    }
}
