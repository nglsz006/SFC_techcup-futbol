package edu.dosw.project.SFC_TechUp_Futbol.core.service;

import edu.dosw.project.SFC_TechUp_Futbol.TestMappers;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.*;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.entity.JugadorEntity;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.mapper.JugadorMapper;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.repository.JugadorJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JugadorServiceExtendedTest {

    private JugadorService service;
    private JugadorJpaRepository jugadorRepo;
    private JugadorMapper jugadorMapper;

    @BeforeEach
    void setUp() {
        jugadorMapper = TestMappers.jugadorMapper();
        Map<String, JugadorEntity> store = new HashMap<>();
        jugadorRepo = mock(JugadorJpaRepository.class);
        when(jugadorRepo.save(any())).thenAnswer(inv -> {
            JugadorEntity e = inv.getArgument(0);
            if (e.getId() == null) e.setId(UUID.randomUUID().toString());
            store.put(e.getId(), e);
            return e;
        });
        when(jugadorRepo.findById(anyString())).thenAnswer(inv -> Optional.ofNullable(store.get(inv.<String>getArgument(0))));
        when(jugadorRepo.findAll()).thenAnswer(inv -> new ArrayList<>(store.values()));
        when(jugadorRepo.existsById(anyString())).thenAnswer(inv -> store.containsKey(inv.<String>getArgument(0)));
        doAnswer(inv -> { store.remove(inv.<String>getArgument(0)); return null; }).when(jugadorRepo).deleteById(anyString());
        service = new JugadorService(jugadorRepo, jugadorMapper);
    }

    private Jugador crearJugador(String email) {
        Jugador j = new Jugador(null, "Juan", email, "pass", Usuario.TipoUsuario.ESTUDIANTE, 10, Jugador.Posicion.DELANTERO, true, "");
        return service.save(j);
    }

    @Test
    void save_nuevo_asignaId() {
        Jugador j = crearJugador("j@test.com");
        assertNotNull(j.getId());
    }

    @Test
    void editarPerfil_nombre_actualiza() {
        Jugador j = crearJugador("j@test.com");
        Jugador editado = service.editarPerfil(j.getId(), "Nuevo", 0, null, "");
        assertEquals("Nuevo", editado.getName());
    }

    @Test
    void editarPerfil_numeroCamiseta_actualiza() {
        Jugador j = crearJugador("j@test.com");
        Jugador editado = service.editarPerfil(j.getId(), "", 99, null, "");
        assertEquals(99, editado.getJerseyNumber());
    }

    @Test
    void editarPerfil_posicion_actualiza() {
        Jugador j = crearJugador("j@test.com");
        Jugador editado = service.editarPerfil(j.getId(), "", 0, Jugador.Posicion.PORTERO, "");
        assertEquals(Jugador.Posicion.PORTERO, editado.getPosition());
    }

    @Test
    void editarPerfil_inexistente_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () ->
                service.editarPerfil("inexistente", "X", 1, Jugador.Posicion.DELANTERO, ""));
    }

    @Test
    void aceptarInvitacion_marcaNoDisponible() {
        Jugador j = crearJugador("j@test.com");
        service.aceptarInvitacion(j.getId());
        assertFalse(service.buscarJugadorPorId(j.getId()).isAvailable());
    }

    @Test
    void rechazarInvitacion_marcaDisponible() {
        Jugador j = crearJugador("j@test.com");
        service.aceptarInvitacion(j.getId());
        service.rechazarInvitacion(j.getId());
        assertTrue(service.buscarJugadorPorId(j.getId()).isAvailable());
    }

    @Test
    void marcarDisponible_sinEquipo_marcaDisponible() {
        Jugador j = new Jugador(null, "Juan", "jdisp@test.com", "pass", Usuario.TipoUsuario.ESTUDIANTE, 10, Jugador.Posicion.DELANTERO, true, "");
        Jugador saved = service.save(j);
        service.marcarDisponible(saved.getId());
        assertTrue(service.buscarJugadorPorId(saved.getId()).isAvailable());
    }

    @Test
    void marcarDisponible_conEquipo_lanzaExcepcion() {
        Jugador j = new Jugador(null, "Juan", "j@test.com", "pass", Usuario.TipoUsuario.ESTUDIANTE, 10, Jugador.Posicion.DELANTERO, false, "equipo-1");
        Jugador saved = service.save(j);
        assertThrows(IllegalStateException.class, () -> service.marcarDisponible(saved.getId()));
    }

    @Test
    void eliminar_sinEquipo_elimina() {
        Jugador j = crearJugador("j@test.com");
        service.eliminar(j.getId());
        assertNull(service.buscarJugadorPorId(j.getId()));
    }

    @Test
    void eliminar_conEquipo_lanzaExcepcion() {
        Jugador j = new Jugador(null, "Juan", "j@test.com", "pass", Usuario.TipoUsuario.ESTUDIANTE, 10, Jugador.Posicion.DELANTERO, false, "");
        j.setEquipo("equipo-1");
        Jugador saved = service.save(j);
        assertThrows(IllegalStateException.class, () -> service.eliminar(saved.getId()));
    }

    @Test
    void buscarJugadorPorId_inexistente_retornaNull() {
        assertNull(service.buscarJugadorPorId("inexistente"));
    }

    @Test
    void getJugadores_retornaLista() {
        crearJugador("j1@test.com");
        crearJugador("j2@test.com");
        assertEquals(2, service.getJugadores().size());
    }
}
