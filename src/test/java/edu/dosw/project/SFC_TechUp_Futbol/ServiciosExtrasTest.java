package edu.dosw.project.SFC_TechUp_Futbol;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.*;
import edu.dosw.project.SFC_TechUp_Futbol.core.repository.*;
import edu.dosw.project.SFC_TechUp_Futbol.core.service.*;
import edu.dosw.project.SFC_TechUp_Futbol.core.util.PasswordUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ServiciosExtrasTest {

    private AlineacionService alineacionService;
    private ArbitroService arbitroService;
    private CapitanService capitanService;
    private OrganizadorService organizadorService;
    private TorneoService torneoService;
    private JugadorService jugadorService;
    private JugadorRepositoryImpl jugadorRepo;

    @BeforeEach
    void setUp() {
        alineacionService = new AlineacionService(new AlineacionRepositoryImpl());
        arbitroService = new ArbitroService(new ArbitroRepositoryImpl());
        jugadorRepo = new JugadorRepositoryImpl();
        jugadorService = new JugadorService(jugadorRepo);
        capitanService = new CapitanService(new CapitanRepositoryImpl(), jugadorService);
        torneoService = new TorneoService(new TorneoRepositoryImpl());
        organizadorService = new OrganizadorService(new OrganizadorRepositoryImpl(), torneoService);
    }

    @Test
    void alineacion_crear_retornaAlineacion() {
        Alineacion a = new Alineacion();
        a.setEquipoId(1);
        a.setPartidoId(1);
        a.setFormacion(Alineacion.Formacion.F_4_4_2);
        a.setTitulares(List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11));
        Alineacion saved = alineacionService.crear(a, Map.of("formacion", "4-4-2", "titulares", List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11)));
        assertTrue(saved.getId() > 0);
    }

    @Test
    void alineacion_obtener_inexistente_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> alineacionService.obtener(99));
    }

    @Test
    void alineacion_listar_retornaLista() {
        assertNotNull(alineacionService.listar());
    }

    @Test
    void arbitro_sinPartidos_retornaListaVacia() {
        assertTrue(arbitroService.consultarPartidosAsignados(99L).isEmpty());
    }

    @Test
    void arbitro_save_retornaArbitro() {
        Arbitro arbitro = new Arbitro(1L, "Ref", "ref@test.com", "pass", Usuario.TipoUsuario.ESTUDIANTE);
        assertNotNull(arbitroService.save(arbitro));
    }

    @Test
    void arbitro_getArbitros_retornaLista() {
        assertNotNull(arbitroService.getArbitros());
    }

    @Test
    void capitan_crearEquipo_capitanInexistente_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> capitanService.crearEquipo(99L, "Los Tigres"));
    }

    @Test
    void capitan_buscarJugadores_posicionVacia_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> capitanService.buscarJugadores(""));
    }

    @Test
    void capitan_buscarJugadores_retornaLista() {
        assertNotNull(capitanService.buscarJugadores("DELANTERO"));
    }

    @Test
    void capitan_definirAlineacion_menosDeSiete_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> capitanService.definirAlineacion(99L, List.of()));
    }

    @Test
    void organizador_crearTorneo_organizadorInexistente_lanzaExcepcion() {
        Torneo t = new Torneo(0, "Copa", LocalDateTime.now(), LocalDateTime.now().plusDays(5), 8, 50);
        assertThrows(IllegalArgumentException.class, () -> organizadorService.crearTorneo(99L, t));
    }

    @Test
    void organizador_iniciarTorneo_sinTorneoActivo_lanzaExcepcion() {
        Organizador org = new Organizador(null, "Org", "org@test.com", "pass", Usuario.TipoUsuario.ESTUDIANTE, null);
        organizadorService.save(org);
        assertThrows(IllegalStateException.class, () -> organizadorService.iniciarTorneo(org.getId()));
    }

    @Test
    void organizador_crearTorneo_nombreInvalido_lanzaExcepcion() {
        Organizador org = new Organizador(null, "Org", "org@test.com", "pass", Usuario.TipoUsuario.ESTUDIANTE, null);
        organizadorService.save(org);
        Torneo t = new Torneo(0, "", LocalDateTime.now(), LocalDateTime.now().plusDays(5), 8, 50);
        assertThrows(IllegalArgumentException.class, () -> organizadorService.crearTorneo(org.getId(), t));
    }

    @Test
    void passwordUtil_cifrarYVerificar_correcto() {
        String hash = PasswordUtil.cifrar("mipassword");
        assertTrue(PasswordUtil.verificar("mipassword", hash));
    }

    @Test
    void passwordUtil_verificar_incorrecta_retornaFalse() {
        String hash = PasswordUtil.cifrar("mipassword");
        assertFalse(PasswordUtil.verificar("otrapassword", hash));
    }

    @Test
    void jugador_editarPerfil_actualizaCampos() {
        Jugador jugador = new Jugador(1L, "Original", "orig@test.com", "pass",
                Usuario.TipoUsuario.ESTUDIANTE, 5, Jugador.Posicion.DEFENSA, true, "");
        jugadorRepo.save(jugador);
        Jugador editado = jugadorService.editarPerfil(1L, "Editado", 10, Jugador.Posicion.PORTERO, "foto.jpg");
        assertEquals("Editado", editado.getName());
        assertEquals(10, editado.getJerseyNumber());
        assertEquals(Jugador.Posicion.PORTERO, editado.getPosition());
    }

    @Test
    void jugador_editarPerfil_inexistente_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () ->
                jugadorService.editarPerfil(99L, "X", 1, Jugador.Posicion.DELANTERO, ""));
    }
}
