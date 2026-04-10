package edu.dosw.project.SFC_TechUp_Futbol.core.service;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Torneo;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.entity.TorneoEntity;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.mapper.TorneoMapper;
import edu.dosw.project.SFC_TechUp_Futbol.TestMappers;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.repository.TorneoJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TorneoServiceTest {

    private TorneoService service;

    @BeforeEach
    void setUp() {
        TorneoMapper mapper = TestMappers.torneoMapper();
        Map<String, TorneoEntity> store = new HashMap<>();
        TorneoJpaRepository repo = mock(TorneoJpaRepository.class);
        when(repo.save(any())).thenAnswer(inv -> {
            TorneoEntity e = inv.getArgument(0);
            if (e.getId() == null) e.setId(UUID.randomUUID().toString());
            store.put(e.getId(), e);
            return e;
        });
        when(repo.findById(anyString())).thenAnswer(inv -> Optional.ofNullable(store.get(inv.<String>getArgument(0))));
        when(repo.findAll()).thenAnswer(inv -> new ArrayList<>(store.values()));
        service = new TorneoService(repo, mapper);
    }

    private Torneo torneoValido(String nombre) {
        return new Torneo(null, nombre,
                LocalDateTime.of(2025, 10, 1, 10, 0),
                LocalDateTime.of(2025, 10, 30, 18, 0), 8, 50.0);
    }

    @Test
    void crear_datosValidos_retornaTorneoEnEstadoCreado() {
        Torneo t = service.crear(torneoValido("Copa Test"), new HashMap<>());
        assertNotNull(t.getId());
        assertEquals(Torneo.EstadoTorneo.CREADO, t.getEstado());
    }

    @Test
    void crear_sinNombre_lanzaExcepcion() {
        Torneo t = new Torneo(null, "", LocalDateTime.now(), LocalDateTime.now().plusDays(5), 8, 50.0);
        assertThrows(IllegalArgumentException.class, () -> service.crear(t, new HashMap<>()));
    }

    @Test
    void crear_unSoloEquipo_lanzaExcepcion() {
        Torneo t = new Torneo(null, "Copa", LocalDateTime.now(), LocalDateTime.now().plusDays(5), 1, 50.0);
        assertThrows(IllegalArgumentException.class, () -> service.crear(t, new HashMap<>()));
    }

    @Test
    void crear_costoNegativo_lanzaExcepcion() {
        Torneo t = new Torneo(null, "Copa", LocalDateTime.now(), LocalDateTime.now().plusDays(5), 8, -10.0);
        assertThrows(IllegalArgumentException.class, () -> service.crear(t, new HashMap<>()));
    }

    @Test
    void crear_fechaInicioDepuesDeFin_lanzaExcepcion() {
        Torneo t = new Torneo(null, "Copa",
                LocalDateTime.of(2025, 11, 1, 10, 0),
                LocalDateTime.of(2025, 10, 1, 10, 0), 8, 50.0);
        assertThrows(IllegalArgumentException.class, () -> service.crear(t, new HashMap<>()));
    }

    @Test
    void obtener_inexistente_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> service.obtener("id-inexistente"));
    }

    @Test
    void configurar_torneoCreado_guardaCampos() {
        Torneo t = service.crear(torneoValido("Copa Config"), new HashMap<>());
        Torneo configurado = service.configurar(t.getId(), "Reglamento A", "Cancha 1",
                "Sabados 10am", "Tarjeta roja = 1 fecha",
                LocalDateTime.of(2025, 9, 25, 0, 0));
        assertEquals("Reglamento A", configurado.getReglamento());
        assertEquals("Cancha 1", configurado.getCanchas());
        assertEquals("Sabados 10am", configurado.getHorarios());
    }

    @Test
    void configurar_cierreInscripcionesInvalido_lanzaExcepcion() {
        Torneo t = service.crear(torneoValido("Copa Cierre"), new HashMap<>());
        assertThrows(IllegalArgumentException.class, () ->
                service.configurar(t.getId(), null, null, null, null,
                        LocalDateTime.of(2025, 10, 5, 0, 0)));
    }

    @Test
    void configurar_torneoEnCurso_lanzaExcepcion() {
        Torneo t = service.crear(torneoValido("Copa EnCurso"), new HashMap<>());
        service.iniciar(t.getId());
        assertThrows(IllegalStateException.class, () ->
                service.configurar(t.getId(), "Reglamento", null, null, null, null));
    }

    @Test
    void configurar_torneoFinalizado_lanzaExcepcion() {
        Torneo t = service.crear(torneoValido("Copa Final"), new HashMap<>());
        service.iniciar(t.getId());
        service.finalizar(t.getId());
        assertThrows(IllegalStateException.class, () ->
                service.configurar(t.getId(), "Reglamento", null, null, null, null));
    }

    @Test
    void iniciar_torneoCreado_cambiaAEnCurso() {
        Torneo t = service.crear(torneoValido("Copa Inicio"), new HashMap<>());
        Torneo iniciado = service.iniciar(t.getId());
        assertEquals(Torneo.EstadoTorneo.EN_CURSO, iniciado.getEstado());
    }

    @Test
    void iniciar_torneoYaEnCurso_lanzaExcepcion() {
        Torneo t = service.crear(torneoValido("Copa YaIniciado"), new HashMap<>());
        service.iniciar(t.getId());
        assertThrows(IllegalStateException.class, () -> service.iniciar(t.getId()));
    }

    @Test
    void iniciar_torneoFinalizado_lanzaExcepcion() {
        Torneo t = service.crear(torneoValido("Copa Finalizado"), new HashMap<>());
        service.iniciar(t.getId());
        service.finalizar(t.getId());
        assertThrows(IllegalStateException.class, () -> service.iniciar(t.getId()));
    }

    @Test
    void finalizar_torneoEnCurso_cambiaAFinalizado() {
        Torneo t = service.crear(torneoValido("Copa Fin"), new HashMap<>());
        service.iniciar(t.getId());
        Torneo finalizado = service.finalizar(t.getId());
        assertEquals(Torneo.EstadoTorneo.FINALIZADO, finalizado.getEstado());
    }

    @Test
    void finalizar_torneoCreado_lanzaExcepcion() {
        Torneo t = service.crear(torneoValido("Copa SinIniciar"), new HashMap<>());
        assertThrows(IllegalStateException.class, () -> service.finalizar(t.getId()));
    }

    @Test
    void finalizar_torneoYaFinalizado_lanzaExcepcion() {
        Torneo t = service.crear(torneoValido("Copa YaFinalizado"), new HashMap<>());
        service.iniciar(t.getId());
        service.finalizar(t.getId());
        assertThrows(IllegalStateException.class, () -> service.finalizar(t.getId()));
    }

    @Test
    void puedeInscribirEquipos_torneoCreado_retornaTrue() {
        Torneo t = service.crear(torneoValido("Copa Inscripcion"), new HashMap<>());
        assertTrue(service.puedeInscribirEquipos(t.getId()));
    }

    @Test
    void puedeInscribirEquipos_torneoEnCurso_retornaFalse() {
        Torneo t = service.crear(torneoValido("Copa EnCursoInsc"), new HashMap<>());
        service.iniciar(t.getId());
        assertFalse(service.puedeInscribirEquipos(t.getId()));
    }

    @Test
    void puedeInscribirEquipos_torneoFinalizado_retornaFalse() {
        Torneo t = service.crear(torneoValido("Copa FinInsc"), new HashMap<>());
        service.iniciar(t.getId());
        service.finalizar(t.getId());
        assertFalse(service.puedeInscribirEquipos(t.getId()));
    }

    @Test
    void flujoCompleto_creacion_configuracion_inicio_finalizacion() {
        Torneo t = service.crear(torneoValido("Copa Completa"), new HashMap<>());
        assertEquals(Torneo.EstadoTorneo.CREADO, t.getEstado());
        assertTrue(service.puedeInscribirEquipos(t.getId()));

        service.configurar(t.getId(), "Reglamento oficial", "Cancha principal",
                "Domingos 9am", "Tarjeta roja = 2 fechas",
                LocalDateTime.of(2025, 9, 28, 0, 0));

        Torneo iniciado = service.iniciar(t.getId());
        assertEquals(Torneo.EstadoTorneo.EN_CURSO, iniciado.getEstado());
        assertFalse(service.puedeInscribirEquipos(t.getId()));

        Torneo finalizado = service.finalizar(t.getId());
        assertEquals(Torneo.EstadoTorneo.FINALIZADO, finalizado.getEstado());
        assertFalse(service.puedeInscribirEquipos(t.getId()));
    }
}
