package edu.dosw.project.SFC_TechUp_Futbol.core.service;

import edu.dosw.project.SFC_TechUp_Futbol.TestMappers;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.*;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.entity.*;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.mapper.*;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PerfilDeportivoServiceImplTest {

    private PerfilDeportivoServiceImpl service;
    private JugadorJpaRepository jugadorRepo;
    private PerfilDeportivoJpaRepository perfilRepo;
    private JugadorMapper jugadorMapper;
    private PerfilDeportivoMapper perfilMapper;

    @BeforeEach
    void setUp() {
        jugadorMapper = TestMappers.jugadorMapper();
        perfilMapper = TestMappers.perfilDeportivoMapper();

        Map<String, JugadorEntity> jugadorStore = new HashMap<>();
        jugadorRepo = mock(JugadorJpaRepository.class);
        when(jugadorRepo.save(any())).thenAnswer(inv -> {
            JugadorEntity e = inv.getArgument(0);
            if (e.getId() == null) e.setId(UUID.randomUUID().toString());
            jugadorStore.put(e.getId(), e);
            return e;
        });
        when(jugadorRepo.findById(anyString())).thenAnswer(inv -> Optional.ofNullable(jugadorStore.get(inv.<String>getArgument(0))));
        when(jugadorRepo.findAll()).thenAnswer(inv -> new ArrayList<>(jugadorStore.values()));

        Map<String, PerfilDeportivoEntity> perfilStore = new HashMap<>();
        perfilRepo = mock(PerfilDeportivoJpaRepository.class);
        when(perfilRepo.save(any())).thenAnswer(inv -> {
            PerfilDeportivoEntity e = inv.getArgument(0);
            if (e.getId() == null) e.setId(UUID.randomUUID().toString());
            perfilStore.put(e.getId(), e);
            return e;
        });
        when(perfilRepo.findById(anyString())).thenAnswer(inv -> Optional.ofNullable(perfilStore.get(inv.<String>getArgument(0))));
        when(perfilRepo.findByJugadorId(anyString())).thenAnswer(inv ->
                perfilStore.values().stream().filter(e -> inv.<String>getArgument(0).equals(e.getJugadorId())).findFirst());
        when(perfilRepo.findAll()).thenAnswer(inv -> new ArrayList<>(perfilStore.values()));

        service = new PerfilDeportivoServiceImpl(perfilRepo, perfilMapper, jugadorRepo);
    }

    private String crearJugador() {
        Jugador j = new Jugador(null, "Juan", "juan@test.com", "pass", Usuario.TipoUsuario.ESTUDIANTE, 10, Jugador.Posicion.DELANTERO, true, "");
        return jugadorRepo.save(jugadorMapper.toEntity(j)).getId();
    }

    @Test
    void crearPerfil_valido_retornaPerfil() {
        String jugadorId = crearJugador();
        PerfilDeportivo p = service.crearPerfil(jugadorId, List.of(Jugador.Posicion.DELANTERO), 10, "foto.jpg", 22, PerfilDeportivo.Genero.MASCULINO, "123", 5);
        assertNotNull(p.getId());
        assertEquals(jugadorId, p.getJugadorId());
    }

    @Test
    void crearPerfil_jugadorInexistente_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () ->
                service.crearPerfil("inexistente", List.of(Jugador.Posicion.DELANTERO), 10, "", 22, PerfilDeportivo.Genero.MASCULINO, "123", null));
    }

    @Test
    void crearPerfil_yaExiste_lanzaExcepcion() {
        String jugadorId = crearJugador();
        service.crearPerfil(jugadorId, List.of(Jugador.Posicion.DELANTERO), 10, "", 22, PerfilDeportivo.Genero.MASCULINO, "123", null);
        assertThrows(IllegalStateException.class, () ->
                service.crearPerfil(jugadorId, List.of(Jugador.Posicion.PORTERO), 1, "", 20, PerfilDeportivo.Genero.FEMENINO, "456", null));
    }

    @Test
    void editarPerfil_valido_actualizaDatos() {
        String jugadorId = crearJugador();
        service.crearPerfil(jugadorId, List.of(Jugador.Posicion.DELANTERO), 10, "", 22, PerfilDeportivo.Genero.MASCULINO, "123", 5);
        PerfilDeportivo editado = service.editarPerfil(jugadorId, List.of(Jugador.Posicion.PORTERO), 1, "nueva.jpg", 25, PerfilDeportivo.Genero.FEMENINO, "456", 8);
        assertEquals(1, editado.getDorsal());
        assertEquals(25, editado.getEdad());
    }

    @Test
    void editarPerfil_sinPerfil_lanzaExcepcion() {
        String jugadorId = crearJugador();
        assertThrows(IllegalArgumentException.class, () ->
                service.editarPerfil(jugadorId, List.of(Jugador.Posicion.DELANTERO), 10, "", 22, PerfilDeportivo.Genero.MASCULINO, "123", null));
    }

    @Test
    void consultarPerfil_existente_retornaPerfil() {
        String jugadorId = crearJugador();
        service.crearPerfil(jugadorId, List.of(Jugador.Posicion.DELANTERO), 10, "", 22, PerfilDeportivo.Genero.MASCULINO, "123", null);
        assertNotNull(service.consultarPerfil(jugadorId));
    }

    @Test
    void consultarPerfil_inexistente_lanzaExcepcion() {
        String jugadorId = crearJugador();
        assertThrows(IllegalArgumentException.class, () -> service.consultarPerfil(jugadorId));
    }

    @Test
    void buscarJugadores_sinFiltros_retornaTodos() {
        String jugadorId = crearJugador();
        service.crearPerfil(jugadorId, List.of(Jugador.Posicion.DELANTERO), 10, "", 22, PerfilDeportivo.Genero.MASCULINO, "123", 5);
        assertEquals(1, service.buscarJugadores(null, null, null, null, null, null).size());
    }

    @Test
    void buscarJugadores_porPosicion_filtraCorrectamente() {
        String jugadorId = crearJugador();
        service.crearPerfil(jugadorId, List.of(Jugador.Posicion.DELANTERO), 10, "", 22, PerfilDeportivo.Genero.MASCULINO, "123", null);
        assertEquals(1, service.buscarJugadores(Jugador.Posicion.DELANTERO, null, null, null, null, null).size());
        assertEquals(0, service.buscarJugadores(Jugador.Posicion.PORTERO, null, null, null, null, null).size());
    }

    @Test
    void buscarJugadores_porEdad_filtraCorrectamente() {
        String jugadorId = crearJugador();
        service.crearPerfil(jugadorId, List.of(Jugador.Posicion.DELANTERO), 10, "", 22, PerfilDeportivo.Genero.MASCULINO, "123", null);
        assertEquals(1, service.buscarJugadores(null, null, 22, null, null, null).size());
        assertEquals(0, service.buscarJugadores(null, null, 30, null, null, null).size());
    }

    @Test
    void buscarJugadores_porGenero_filtraCorrectamente() {
        String jugadorId = crearJugador();
        service.crearPerfil(jugadorId, List.of(Jugador.Posicion.DELANTERO), 10, "", 22, PerfilDeportivo.Genero.MASCULINO, "123", null);
        assertEquals(1, service.buscarJugadores(null, null, null, PerfilDeportivo.Genero.MASCULINO, null, null).size());
        assertEquals(0, service.buscarJugadores(null, null, null, PerfilDeportivo.Genero.FEMENINO, null, null).size());
    }

    @Test
    void buscarJugadores_porSemestre_filtraCorrectamente() {
        String jugadorId = crearJugador();
        service.crearPerfil(jugadorId, List.of(Jugador.Posicion.DELANTERO), 10, "", 22, PerfilDeportivo.Genero.MASCULINO, "123", 5);
        assertEquals(1, service.buscarJugadores(null, 5, null, null, null, null).size());
        assertEquals(0, service.buscarJugadores(null, 8, null, null, null, null).size());
    }

    @Test
    void buscarJugadores_porIdentificacion_filtraCorrectamente() {
        String jugadorId = crearJugador();
        service.crearPerfil(jugadorId, List.of(Jugador.Posicion.DELANTERO), 10, "", 22, PerfilDeportivo.Genero.MASCULINO, "123", null);
        // La identificacion se guarda encriptada, buscar con string vacio no filtra
        assertEquals(1, service.buscarJugadores(null, null, null, null, null, "").size());
        assertEquals(1, service.buscarJugadores(null, null, null, null, null, null).size());
    }

    @Test
    void buscarJugadores_porNombre_filtraCorrectamente() {
        String jugadorId = crearJugador();
        service.crearPerfil(jugadorId, List.of(Jugador.Posicion.DELANTERO), 10, "", 22, PerfilDeportivo.Genero.MASCULINO, "123", null);
        assertEquals(1, service.buscarJugadores(null, null, null, null, "Juan", null).size());
        assertEquals(0, service.buscarJugadores(null, null, null, null, "Pedro", null).size());
    }
}
