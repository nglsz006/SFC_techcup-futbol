package edu.dosw.project.SFC_TechUp_Futbol.core.service;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.*;
import edu.dosw.project.SFC_TechUp_Futbol.core.service.*;
import edu.dosw.project.SFC_TechUp_Futbol.core.util.PasswordUtil;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.entity.*;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.mapper.*;
import edu.dosw.project.SFC_TechUp_Futbol.TestMappers;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import org.springframework.test.util.ReflectionTestUtils;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ServiciosExtrasTest {

    private AlineacionService alineacionService;
    private ArbitroService arbitroService;
    private CapitanService capitanService;
    private OrganizadorService organizadorService;
    private TorneoService torneoService;
    private JugadorService jugadorService;
    private JugadorJpaRepository jugadorRepo;
    private JugadorMapper jugadorMapper;

    @BeforeEach
    void setUp() {
        TorneoMapper torneoMapper = TestMappers.torneoMapper();
        EquipoMapper equipoMapper = TestMappers.equipoMapper();
        jugadorMapper = TestMappers.jugadorMapper();
        PartidoMapper partidoMapper = TestMappers.partidoMapper(jugadorMapper);
        AlineacionMapper alineacionMapper = TestMappers.alineacionMapper();

        Map<String, AlineacionEntity> alineacionStore = new HashMap<>();
        AlineacionJpaRepository alineacionRepo = mock(AlineacionJpaRepository.class);
        when(alineacionRepo.save(any())).thenAnswer(inv -> {
            AlineacionEntity e = inv.getArgument(0);
            if (e.getId() == null) e.setId(UUID.randomUUID().toString());
            alineacionStore.put(e.getId(), e);
            return e;
        });
        when(alineacionRepo.findById(anyString())).thenAnswer(inv -> Optional.ofNullable(alineacionStore.get(inv.<String>getArgument(0))));
        when(alineacionRepo.findAll()).thenAnswer(inv -> new ArrayList<>(alineacionStore.values()));
        alineacionService = new AlineacionService(alineacionRepo, alineacionMapper,
                mock(EquipoJpaRepository.class), equipoMapper, mock(PartidoJpaRepository.class), partidoMapper);

        Map<String, ArbitroEntity> arbitroStore = new HashMap<>();
        ArbitroJpaRepository arbitroRepository = mock(ArbitroJpaRepository.class);
        ArbitroMapper arbitroMapper = TestMappers.arbitroMapper(partidoMapper);
        when(arbitroRepository.save(any())).thenAnswer(inv -> {
            ArbitroEntity e = inv.getArgument(0);
            if (e.getId() == null) e.setId(UUID.randomUUID().toString());
            arbitroStore.put(e.getId(), e);
            return e;
        });
        when(arbitroRepository.findById(anyString())).thenAnswer(inv -> Optional.ofNullable(arbitroStore.get(inv.<String>getArgument(0))));
        when(arbitroRepository.findAll()).thenAnswer(inv -> new ArrayList<>(arbitroStore.values()));
        arbitroService = new ArbitroService(arbitroRepository, arbitroMapper);

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
        when(jugadorRepo.existsById(anyString())).thenAnswer(inv -> jugadorStore.containsKey(inv.<String>getArgument(0)));
        doAnswer(inv -> { jugadorStore.remove(inv.<String>getArgument(0)); return null; }).when(jugadorRepo).deleteById(anyString());
        jugadorService = new JugadorService(jugadorRepo, jugadorMapper);

        Map<String, CapitanEntity> capitanStore = new HashMap<>();
        CapitanJpaRepository capitanRepository = mock(CapitanJpaRepository.class);
        EquipoJpaRepository equipoRepo = mock(EquipoJpaRepository.class);
        CapitanMapper capitanMapper = TestMappers.capitanMapper(equipoRepo, equipoMapper);
        when(capitanRepository.save(any())).thenAnswer(inv -> {
            CapitanEntity e = inv.getArgument(0);
            if (e.getId() == null) e.setId(UUID.randomUUID().toString());
            capitanStore.put(e.getId(), e);
            return e;
        });
        when(capitanRepository.findById(anyString())).thenAnswer(inv -> Optional.ofNullable(capitanStore.get(inv.<String>getArgument(0))));
        when(capitanRepository.findAll()).thenAnswer(inv -> new ArrayList<>(capitanStore.values()));
        when(capitanRepository.existsById(anyString())).thenAnswer(inv -> capitanStore.containsKey(inv.<String>getArgument(0)));
        doAnswer(inv -> { capitanStore.remove(inv.<String>getArgument(0)); return null; }).when(capitanRepository).deleteById(anyString());
        capitanService = new CapitanService(capitanRepository, capitanMapper, jugadorService, jugadorRepo, jugadorMapper);

        Map<String, TorneoEntity> torneoStore = new HashMap<>();
        TorneoJpaRepository torneoRepository = mock(TorneoJpaRepository.class);
        when(torneoRepository.save(any())).thenAnswer(inv -> {
            TorneoEntity e = inv.getArgument(0);
            if (e.getId() == null) e.setId(UUID.randomUUID().toString());
            torneoStore.put(e.getId(), e);
            return e;
        });
        when(torneoRepository.findById(anyString())).thenAnswer(inv -> Optional.ofNullable(torneoStore.get(inv.<String>getArgument(0))));
        when(torneoRepository.findAll()).thenAnswer(inv -> new ArrayList<>(torneoStore.values()));
        torneoService = new TorneoService(torneoRepository, torneoMapper);

        Map<String, OrganizadorEntity> orgStore = new HashMap<>();
        OrganizadorJpaRepository orgRepository = mock(OrganizadorJpaRepository.class);
        OrganizadorMapper orgMapper = TestMappers.organizadorMapper(torneoRepository, torneoMapper);
        when(orgRepository.save(any())).thenAnswer(inv -> {
            OrganizadorEntity e = inv.getArgument(0);
            if (e.getId() == null) e.setId(UUID.randomUUID().toString());
            orgStore.put(e.getId(), e);
            return e;
        });
        when(orgRepository.findById(anyString())).thenAnswer(inv -> Optional.ofNullable(orgStore.get(inv.<String>getArgument(0))));
        when(orgRepository.findAll()).thenAnswer(inv -> new ArrayList<>(orgStore.values()));
        organizadorService = new OrganizadorService(orgRepository, orgMapper, torneoService);
    }

    @Test
    void alineacion_crear_retornaAlineacion() {
        Alineacion a = new Alineacion();
        a.setEquipoId("uuid-equipo-1");
        a.setPartidoId("uuid-partido-1");
        a.setFormacion(Alineacion.Formacion.F_4_4_2);
        a.setTitulares(List.of("j1", "j2", "j3", "j4", "j5", "j6", "j7", "j8", "j9", "j10", "j11"));
        Alineacion saved = alineacionService.crear(a, Map.of("formacion", "4-4-2", "titulares", List.of("j1", "j2", "j3", "j4", "j5", "j6", "j7", "j8", "j9", "j10", "j11")));
        assertNotNull(saved.getId());
    }

    @Test
    void alineacion_obtener_inexistente_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> alineacionService.obtener("uuid-inexistente"));
    }

    @Test
    void alineacion_listar_retornaLista() {
        assertNotNull(alineacionService.listar());
    }

    @Test
    void arbitro_sinPartidos_retornaListaVacia() {
        assertTrue(arbitroService.consultarPartidosAsignados("uuid-inexistente").isEmpty());
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
        assertThrows(IllegalArgumentException.class, () -> capitanService.crearEquipo("uuid-inexistente", "Los Tigres"));
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
        assertThrows(IllegalArgumentException.class, () -> capitanService.definirAlineacion("uuid-inexistente", List.of()));
    }

    @Test
    void organizador_crearTorneo_organizadorInexistente_lanzaExcepcion() {
        Torneo t = new Torneo(null, "Copa", LocalDateTime.now(), LocalDateTime.now().plusDays(5), 8, 50);
        assertThrows(IllegalArgumentException.class, () -> organizadorService.crearTorneo("uuid-inexistente", t));
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
        Torneo t = new Torneo(null, "", LocalDateTime.now(), LocalDateTime.now().plusDays(5), 8, 50);
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
        JugadorEntity saved = jugadorRepo.save(jugadorMapper.toEntity(jugador));
        jugador.setId(saved.getId());
        Jugador editado = jugadorService.editarPerfil(jugador.getId(), "Editado", 10, Jugador.Posicion.PORTERO, "foto.jpg");
        assertEquals("Editado", editado.getName());
        assertEquals(10, editado.getJerseyNumber());
        assertEquals(Jugador.Posicion.PORTERO, editado.getPosition());
    }

    @Test
    void jugador_editarPerfil_inexistente_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () ->
                jugadorService.editarPerfil("uuid-inexistente", "X", 1, Jugador.Posicion.DELANTERO, ""));
    }
}
