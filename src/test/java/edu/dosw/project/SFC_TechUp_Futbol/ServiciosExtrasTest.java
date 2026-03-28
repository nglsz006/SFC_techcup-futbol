package edu.dosw.project.SFC_TechUp_Futbol;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.*;
import edu.dosw.project.SFC_TechUp_Futbol.core.repository.*;
import edu.dosw.project.SFC_TechUp_Futbol.core.service.*;
import edu.dosw.project.SFC_TechUp_Futbol.core.util.PasswordUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ServiciosExtrasTest {

    private AlineacionService alineacionService;
    private ArbitroService arbitroService;
    private CapitanService capitanService;
    private OrganizadorService organizadorService;
    private TorneoService torneoService;
    private JugadorService jugadorService;
    private JugadorRepository jugadorRepo;

    @BeforeEach
    void setUp() {
        Map<Integer, Alineacion> alineacionStore = new HashMap<>();
        AtomicInteger alineacionIdGen = new AtomicInteger(1);
        AlineacionRepository alineacionRepo = mock(AlineacionRepository.class);
        when(alineacionRepo.save(any())).thenAnswer(inv -> {
            Alineacion a = inv.getArgument(0);
            if (a.getId() == 0) a.setId(alineacionIdGen.getAndIncrement());
            alineacionStore.put(a.getId(), a);
            return a;
        });
        when(alineacionRepo.findById(anyInt())).thenAnswer(inv -> Optional.ofNullable(alineacionStore.get(inv.<Integer>getArgument(0))));
        when(alineacionRepo.findAll()).thenAnswer(inv -> new ArrayList<>(alineacionStore.values()));
        alineacionService = new AlineacionService(alineacionRepo);

        Map<Long, Arbitro> arbitroStore = new HashMap<>();
        AtomicLong arbitroIdGen = new AtomicLong(1);
        ArbitroRepository arbitroRepository = mock(ArbitroRepository.class);
        when(arbitroRepository.save(any())).thenAnswer(inv -> {
            Arbitro a = inv.getArgument(0);
            if (a.getId() == null) a.setId(arbitroIdGen.getAndIncrement());
            arbitroStore.put(a.getId(), a);
            return a;
        });
        when(arbitroRepository.findById(anyLong())).thenAnswer(inv -> Optional.ofNullable(arbitroStore.get(inv.<Long>getArgument(0))));
        when(arbitroRepository.findAll()).thenAnswer(inv -> new ArrayList<>(arbitroStore.values()));
        arbitroService = new ArbitroService(arbitroRepository);

        Map<Long, Jugador> jugadorStore = new HashMap<>();
        AtomicLong jugadorIdGen = new AtomicLong(1);
        jugadorRepo = mock(JugadorRepository.class);
        when(jugadorRepo.save(any())).thenAnswer(inv -> {
            Jugador j = inv.getArgument(0);
            if (j.getId() == null) j.setId(jugadorIdGen.getAndIncrement());
            jugadorStore.put(j.getId(), j);
            return j;
        });
        when(jugadorRepo.findById(anyLong())).thenAnswer(inv -> Optional.ofNullable(jugadorStore.get(inv.<Long>getArgument(0))));
        when(jugadorRepo.findAll()).thenAnswer(inv -> new ArrayList<>(jugadorStore.values()));
        jugadorService = new JugadorService(jugadorRepo);

        Map<Long, Capitan> capitanStore = new HashMap<>();
        AtomicLong capitanIdGen = new AtomicLong(1);
        CapitanRepository capitanRepository = mock(CapitanRepository.class);
        when(capitanRepository.save(any())).thenAnswer(inv -> {
            Capitan c = inv.getArgument(0);
            if (c.getId() == null) c.setId(capitanIdGen.getAndIncrement());
            capitanStore.put(c.getId(), c);
            return c;
        });
        when(capitanRepository.findById(anyLong())).thenAnswer(inv -> Optional.ofNullable(capitanStore.get(inv.<Long>getArgument(0))));
        when(capitanRepository.findAll()).thenAnswer(inv -> new ArrayList<>(capitanStore.values()));
        capitanService = new CapitanService(capitanRepository, jugadorService);

        Map<Integer, Torneo> torneoStore = new HashMap<>();
        AtomicInteger torneoIdGen = new AtomicInteger(1);
        TorneoRepository torneoRepository = mock(TorneoRepository.class);
        when(torneoRepository.save(any())).thenAnswer(inv -> {
            Torneo t = inv.getArgument(0);
            if (t.getId() == 0) t.setId(torneoIdGen.getAndIncrement());
            torneoStore.put(t.getId(), t);
            return t;
        });
        when(torneoRepository.findById(anyInt())).thenAnswer(inv -> Optional.ofNullable(torneoStore.get(inv.<Integer>getArgument(0))));
        when(torneoRepository.findAll()).thenAnswer(inv -> new ArrayList<>(torneoStore.values()));
        torneoService = new TorneoService(torneoRepository);

        Map<Long, Organizador> orgStore = new HashMap<>();
        AtomicLong orgIdGen = new AtomicLong(1);
        OrganizadorRepository orgRepository = mock(OrganizadorRepository.class);
        when(orgRepository.save(any())).thenAnswer(inv -> {
            Organizador o = inv.getArgument(0);
            if (o.getId() == null) o.setId(orgIdGen.getAndIncrement());
            orgStore.put(o.getId(), o);
            return o;
        });
        when(orgRepository.findById(anyLong())).thenAnswer(inv -> Optional.ofNullable(orgStore.get(inv.<Long>getArgument(0))));
        when(orgRepository.findAll()).thenAnswer(inv -> new ArrayList<>(orgStore.values()));
        organizadorService = new OrganizadorService(orgRepository, torneoService);
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
        Arbitro arbitro = new Arbitro(null, "Ref", "ref@test.com", "pass", Usuario.TipoUsuario.ESTUDIANTE);
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
        Jugador jugador = new Jugador(null, "Original", "orig@test.com", "pass",
                Usuario.TipoUsuario.ESTUDIANTE, 5, Jugador.Posicion.DEFENSA, true, "");
        jugadorRepo.save(jugador);
        Jugador editado = jugadorService.editarPerfil(jugador.getId(), "Editado", 10, Jugador.Posicion.PORTERO, "foto.jpg");
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
