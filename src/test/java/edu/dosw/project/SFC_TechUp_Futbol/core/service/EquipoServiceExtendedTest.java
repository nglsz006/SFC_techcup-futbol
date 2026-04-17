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

class EquipoServiceExtendedTest {

    private EquipoService service;
    private EquipoJpaRepository equipoRepo;
    private JugadorJpaRepository jugadorRepo;
    private TorneoJpaRepository torneoRepo;
    private EquipoMapper equipoMapper;
    private JugadorMapper jugadorMapper;

    @BeforeEach
    void setUp() {
        equipoMapper = TestMappers.equipoMapper();
        jugadorMapper = TestMappers.jugadorMapper();

        Map<String, EquipoEntity> equipoStore = new HashMap<>();
        equipoRepo = mock(EquipoJpaRepository.class);
        when(equipoRepo.save(any())).thenAnswer(inv -> {
            EquipoEntity e = inv.getArgument(0);
            if (e.getId() == null) e.setId(UUID.randomUUID().toString());
            equipoStore.put(e.getId(), e);
            return e;
        });
        when(equipoRepo.findById(anyString())).thenAnswer(inv -> Optional.ofNullable(equipoStore.get(inv.<String>getArgument(0))));
        when(equipoRepo.findAll()).thenAnswer(inv -> new ArrayList<>(equipoStore.values()));

        Map<String, JugadorEntity> jugadorStore = new HashMap<>();
        jugadorRepo = mock(JugadorJpaRepository.class);
        when(jugadorRepo.findById(anyString())).thenAnswer(inv -> Optional.ofNullable(jugadorStore.get(inv.<String>getArgument(0))));
        when(jugadorRepo.findAll()).thenAnswer(inv -> new ArrayList<>(jugadorStore.values()));

        torneoRepo = mock(TorneoJpaRepository.class);
        when(torneoRepo.findByEstado(any())).thenReturn(new ArrayList<>());

        service = new EquipoService(equipoRepo, equipoMapper);
        org.springframework.test.util.ReflectionTestUtils.setField(service, "jugadorRepository", jugadorRepo);
        org.springframework.test.util.ReflectionTestUtils.setField(service, "torneoRepository", torneoRepo);
    }

    private Equipo crearEquipo(String nombre) {
        return service.crear(new Equipo(null, nombre, "", "rojo", "blanco", "cap-1"),
                Map.of("nombre", nombre, "colorPrincipal", "rojo"));
    }

    private JugadorEntity crearJugadorEntity(String id, Usuario.Carrera carrera, Usuario.TipoUsuario tipo) {
        JugadorEntity e = new JugadorEntity();
        e.setId(id);
        e.setName("Jugador " + id);
        e.setEmail("j" + id + "@test.com");
        e.setPassword("pass");
        e.setUserType(tipo);
        e.setCarrera(carrera);
        e.setAvailable(true);
        return e;
    }

    @Test
    void crear_valido_retornaEquipo() {
        Equipo e = crearEquipo("Los Tigres");
        assertNotNull(e.getId());
    }

    @Test
    void agregarJugador_sinTorneoEnCurso_agrega() {
        Equipo e = crearEquipo("Los Tigres");
        service.agregarJugador(e.getId(), "jugador-1");
        assertEquals(1, service.obtener(e.getId()).getJugadores().size());
    }

    @Test
    void agregarJugador_conTorneoEnCurso_lanzaExcepcion() {
        TorneoEntity torneoEnCurso = new TorneoEntity();
        torneoEnCurso.setId("torneo-1");
        torneoEnCurso.setEstado(Torneo.EstadoTorneo.EN_CURSO);
        when(torneoRepo.findByEstado(Torneo.EstadoTorneo.EN_CURSO)).thenReturn(List.of(torneoEnCurso));

        Equipo e = crearEquipo("Los Tigres");
        assertThrows(IllegalStateException.class, () -> service.agregarJugador(e.getId(), "jugador-1"));
    }

    @Test
    void validarComposicion_menosDeOcho_retornaInvalido() {
        Equipo e = crearEquipo("Pocos");
        for (int i = 1; i <= 5; i++) service.agregarJugador(e.getId(), "j" + i);
        var resultado = service.validarComposicion(e.getId());
        assertEquals(false, resultado.get("valido"));
    }

    @Test
    void validarComposicion_ochoJugadores_retornaValido() {
        Equipo e = crearEquipo("Suficientes");
        Map<String, JugadorEntity> jugadorStore = new HashMap<>();
        for (int i = 1; i <= 8; i++) {
            String id = "j" + i;
            JugadorEntity je = crearJugadorEntity(id, Usuario.Carrera.INGENIERIA_SISTEMAS, Usuario.TipoUsuario.ESTUDIANTE);
            jugadorStore.put(id, je);
            service.agregarJugador(e.getId(), id);
        }
        when(jugadorRepo.findById(anyString())).thenAnswer(inv -> Optional.ofNullable(jugadorStore.get(inv.<String>getArgument(0))));
        var resultado = service.validarComposicion(e.getId());
        assertEquals(true, resultado.get("valido"));
    }

    @Test
    void validarComposicion_tipoNoPermitido_retornaInvalido() {
        Equipo e = crearEquipo("Invalidos");
        Map<String, JugadorEntity> jugadorStore = new HashMap<>();
        for (int i = 1; i <= 8; i++) {
            String id = "j" + i;
            // Tipo no permitido simulado con null
            JugadorEntity je = crearJugadorEntity(id, null, null);
            jugadorStore.put(id, je);
            service.agregarJugador(e.getId(), id);
        }
        when(jugadorRepo.findById(anyString())).thenAnswer(inv -> Optional.ofNullable(jugadorStore.get(inv.<String>getArgument(0))));
        var resultado = service.validarComposicion(e.getId());
        assertEquals(false, resultado.get("valido"));
    }

    @Test
    void validarComposicion_pocosDeCarreraPrioritaria_retornaInvalido() {
        Equipo e = crearEquipo("Mixtos");
        Map<String, JugadorEntity> jugadorStore = new HashMap<>();
        for (int i = 1; i <= 8; i++) {
            String id = "j" + i;
            // Solo 1 de carrera prioritaria, los demás de maestría
            Usuario.Carrera carrera = i == 1 ? Usuario.Carrera.INGENIERIA_SISTEMAS : Usuario.Carrera.MAESTRIA_INFORMATICA;
            JugadorEntity je = crearJugadorEntity(id, carrera, Usuario.TipoUsuario.ESTUDIANTE);
            jugadorStore.put(id, je);
            service.agregarJugador(e.getId(), id);
        }
        when(jugadorRepo.findById(anyString())).thenAnswer(inv -> Optional.ofNullable(jugadorStore.get(inv.<String>getArgument(0))));
        var resultado = service.validarComposicion(e.getId());
        assertEquals(false, resultado.get("valido"));
    }

    @Test
    void eliminar_equipoExistente_elimina() {
        Equipo e = crearEquipo("Los Tigres");
        doNothing().when(equipoRepo).deleteById(anyString());
        service.eliminar(e.getId());
        verify(equipoRepo).deleteById(e.getId());
    }

    @Test
    void obtener_inexistente_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> service.obtener("inexistente"));
    }
}
