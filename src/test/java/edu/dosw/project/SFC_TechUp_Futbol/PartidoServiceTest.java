package edu.dosw.project.SFC_TechUp_Futbol;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.*;
import edu.dosw.project.SFC_TechUp_Futbol.core.repository.*;
import edu.dosw.project.SFC_TechUp_Futbol.core.service.PartidoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class PartidoServiceTest {

    private PartidoServiceImpl service;
    private TorneoRepositoryImpl torneoRepo;
    private EquipoRepositoryImpl equipoRepo;
    private JugadorRepositoryImpl jugadorRepo;

    private Torneo torneo;
    private Equipo local;
    private Equipo visitante;

    @BeforeEach
    void setUp() {
        torneoRepo = new TorneoRepositoryImpl();
        equipoRepo = new EquipoRepositoryImpl();
        jugadorRepo = new JugadorRepositoryImpl();
        service = new PartidoServiceImpl(new PartidoRepositoryImpl(), torneoRepo, equipoRepo, jugadorRepo);

        torneo = torneoRepo.save(new Torneo(0, "Copa", LocalDateTime.now(), LocalDateTime.now().plusDays(5), 8, 50));
        local = equipoRepo.save(new Equipo(0, "Local", "", "rojo", "blanco", 1));
        visitante = equipoRepo.save(new Equipo(0, "Visitante", "", "azul", "negro", 2));
    }

    @Test
    void crearPartido_datosValidos_retornaPartido() {
        Partido p = service.crearPartido((long) torneo.getId(), (long) local.getId(), (long) visitante.getId(), LocalDateTime.now(), "cancha 1");
        assertNotNull(p.getId());
        assertEquals(Partido.PartidoEstado.PROGRAMADO, p.getEstado());
    }

    @Test
    void crearPartido_mismoEquipo_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () ->
                service.crearPartido((long) torneo.getId(), (long) local.getId(), (long) local.getId(), LocalDateTime.now(), "cancha 1"));
    }

    @Test
    void crearPartido_torneoInexistente_lanzaExcepcion() {
        assertThrows(RuntimeException.class, () ->
                service.crearPartido(99L, (long) local.getId(), (long) visitante.getId(), LocalDateTime.now(), "cancha 1"));
    }

    @Test
    void iniciarPartido_estadoProgramado_cambaAEnCurso() {
        Partido p = service.crearPartido((long) torneo.getId(), (long) local.getId(), (long) visitante.getId(), LocalDateTime.now(), "cancha 1");
        Partido iniciado = service.iniciarPartido(p.getId());
        assertEquals(Partido.PartidoEstado.EN_CURSO, iniciado.getEstado());
    }

    @Test
    void registrarResultado_enCurso_actualizaMarcador() {
        Partido p = service.crearPartido((long) torneo.getId(), (long) local.getId(), (long) visitante.getId(), LocalDateTime.now(), "cancha 1");
        service.iniciarPartido(p.getId());
        Partido resultado = service.registrarResultado(p.getId(), 2, 1);
        assertEquals(2, resultado.getMarcadorLocal());
        assertEquals(1, resultado.getMarcadorVisitante());
    }

    @Test
    void finalizarPartido_enCurso_cambaAFinalizado() {
        Partido p = service.crearPartido((long) torneo.getId(), (long) local.getId(), (long) visitante.getId(), LocalDateTime.now(), "cancha 1");
        service.iniciarPartido(p.getId());
        Partido finalizado = service.finalizarPartido(p.getId());
        assertEquals(Partido.PartidoEstado.FINALIZADO, finalizado.getEstado());
    }

    @Test
    void registrarGoleador_enCurso_agregaGol() {
        Jugador jugador = new Jugador(1L, "Juan", "juan@test.com", "pass", Usuario.TipoUsuario.ESTUDIANTE, 10, Jugador.Posicion.DELANTERO, true, "");
        jugadorRepo.save(jugador);
        Partido p = service.crearPartido((long) torneo.getId(), (long) local.getId(), (long) visitante.getId(), LocalDateTime.now(), "cancha 1");
        service.iniciarPartido(p.getId());
        Partido con_gol = service.registrarGoleador(p.getId(), 1L, 30);
        assertEquals(1, con_gol.getGoles().size());
    }

    @Test
    void registrarGoleador_noEnCurso_lanzaExcepcion() {
        Jugador jugador = new Jugador(1L, "Juan", "juan@test.com", "pass", Usuario.TipoUsuario.ESTUDIANTE, 10, Jugador.Posicion.DELANTERO, true, "");
        jugadorRepo.save(jugador);
        Partido p = service.crearPartido((long) torneo.getId(), (long) local.getId(), (long) visitante.getId(), LocalDateTime.now(), "cancha 1");
        assertThrows(IllegalStateException.class, () -> service.registrarGoleador(p.getId(), 1L, 30));
    }

    @Test
    void registrarSancion_enCurso_agregaSancionAlPartidoYAlJugador() {
        Jugador jugador = new Jugador(1L, "Juan", "juan@test.com", "pass", Usuario.TipoUsuario.ESTUDIANTE, 10, Jugador.Posicion.DELANTERO, true, "");
        jugadorRepo.save(jugador);
        Partido p = service.crearPartido((long) torneo.getId(), (long) local.getId(), (long) visitante.getId(), LocalDateTime.now(), "cancha 1");
        service.iniciarPartido(p.getId());
        Partido conSancion = service.registrarSancion(p.getId(), 1L, Sancion.TipoSancion.TARJETA_AMARILLA, "Falta reiterada sobre el rival");
        assertEquals(1, conSancion.getSanciones().size());
        assertEquals(Sancion.TipoSancion.TARJETA_AMARILLA, conSancion.getSanciones().get(0).getTipoSancion());
        assertEquals("Falta reiterada sobre el rival", conSancion.getSanciones().get(0).getDescripcion());
        assertTrue(jugador.tieneSancion(Sancion.TipoSancion.TARJETA_AMARILLA));
    }

    @Test
    void registrarSancion_noEnCurso_lanzaExcepcion() {
        Jugador jugador = new Jugador(1L, "Juan", "juan@test.com", "pass", Usuario.TipoUsuario.ESTUDIANTE, 10, Jugador.Posicion.DELANTERO, true, "");
        jugadorRepo.save(jugador);
        Partido p = service.crearPartido((long) torneo.getId(), (long) local.getId(), (long) visitante.getId(), LocalDateTime.now(), "cancha 1");
        assertThrows(IllegalStateException.class, () ->
                service.registrarSancion(p.getId(), 1L, Sancion.TipoSancion.TARJETA_ROJA, "Juego brusco"));
    }

    @Test
    void registrarSancion_agresionVerbal_agregaCorrectamente() {
        Jugador jugador = new Jugador(1L, "Juan", "juan@test.com", "pass", Usuario.TipoUsuario.ESTUDIANTE, 10, Jugador.Posicion.DELANTERO, true, "");
        jugadorRepo.save(jugador);
        Partido p = service.crearPartido((long) torneo.getId(), (long) local.getId(), (long) visitante.getId(), LocalDateTime.now(), "cancha 1");
        service.iniciarPartido(p.getId());
        service.registrarSancion(p.getId(), 1L, Sancion.TipoSancion.AGRESION_VERBAL, "Insultos al árbitro en el minuto 35");
        assertTrue(jugador.tieneSancion(Sancion.TipoSancion.AGRESION_VERBAL));
        assertEquals(1, jugador.getSancionesPorTipo(Sancion.TipoSancion.AGRESION_VERBAL).size());
    }

    @Test
    void registrarSancion_multiplesSancionesAlMismoJugador_acumula() {
        Jugador jugador = new Jugador(1L, "Juan", "juan@test.com", "pass", Usuario.TipoUsuario.ESTUDIANTE, 10, Jugador.Posicion.DELANTERO, true, "");
        jugadorRepo.save(jugador);
        Partido p = service.crearPartido((long) torneo.getId(), (long) local.getId(), (long) visitante.getId(), LocalDateTime.now(), "cancha 1");
        service.iniciarPartido(p.getId());
        service.registrarSancion(p.getId(), 1L, Sancion.TipoSancion.TARJETA_AMARILLA, "Primera falta en minuto 20");
        service.registrarSancion(p.getId(), 1L, Sancion.TipoSancion.TARJETA_AMARILLA, "Segunda falta en minuto 60");
        assertEquals(2, jugador.getSancionesPorTipo(Sancion.TipoSancion.TARJETA_AMARILLA).size());
        assertEquals(2, jugador.getSanciones().size());
    }

    @Test
    void consultarPartido_existente_retornaPartido() {
        Partido p = service.crearPartido((long) torneo.getId(), (long) local.getId(), (long) visitante.getId(), LocalDateTime.now(), "cancha 1");
        assertNotNull(service.consultarPartido(p.getId()));
    }

    @Test
    void consultarPartido_inexistente_lanzaExcepcion() {
        assertThrows(RuntimeException.class, () -> service.consultarPartido(99L));
    }

    @Test
    void consultarPartidosPorTorneo_retornaLista() {
        service.crearPartido((long) torneo.getId(), (long) local.getId(), (long) visitante.getId(), LocalDateTime.now(), "cancha 1");
        assertEquals(1, service.consultarPartidosPorTorneo((long) torneo.getId()).size());
    }

    @Test
    void consultarPartidosPorEquipo_retornaLista() {
        service.crearPartido((long) torneo.getId(), (long) local.getId(), (long) visitante.getId(), LocalDateTime.now(), "cancha 1");
        assertFalse(service.consultarPartidosPorEquipo((long) local.getId()).isEmpty());
    }
}