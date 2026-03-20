package edu.dosw.project.SFC_TechUp_Futbol;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.*;
import edu.dosw.project.SFC_TechUp_Futbol.core.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.*;
import edu.dosw.project.SFC_TechUp_Futbol.core.service.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("PartidoServiceImpl - pruebas unitarias")
class PartidoServiceImplTest {

    @Mock private PartidoRepository partidoRepository;
    @Mock private TorneoRepository torneoRepository;
    @Mock private EquipoRepository equipoRepository;
    @Mock private JugadorRepository jugadorRepository;

    @InjectMocks
    private PartidoServiceImpl partidoService;

    private Torneo torneo;
    private Equipo local;
    private Equipo visitante;
    private Jugador jugador;
    private LocalDateTime fecha;

    @BeforeEach
    void setUp() {
        torneo = new Torneo();
        torneo.setId(1);
        torneo.setNombre("Copa TechUp");

        local = new Equipo();
        local.setId(1);
        local.setNombre("Equipo Local");

        visitante = new Equipo();
        visitante.setId(2);
        visitante.setNombre("Equipo Visitante");

        jugador = new Jugador();
        jugador.setId(10L);
        jugador.setName("Juan Perez");
        jugador.setEquipo(1);

        fecha = LocalDateTime.of(2025, 9, 15, 16, 0);
    }

    @Test
    @DisplayName("crearPartido - crea y guarda partido correctamente")
    void crearPartido_datosValidos_creaPartidoEnEstadoProgramado() {
        when(torneoRepository.findById(1)).thenReturn(Optional.of(torneo));
        when(equipoRepository.findById(1)).thenReturn(Optional.of(local));
        when(equipoRepository.findById(2)).thenReturn(Optional.of(visitante));
        when(partidoRepository.save(any())).thenAnswer(inv -> {
            Partido p = inv.getArgument(0);
            p.setId(100L);
            return p;
        });

        Partido resultado = partidoService.crearPartido(1L, 1L, 2L, fecha, "Cancha Norte");

        assertThat(resultado).isNotNull();
        assertThat(resultado.getEstado()).isEqualTo(Partido.PartidoEstado.PROGRAMADO);
        assertThat(resultado.getEquipoLocal()).isEqualTo(local);
        assertThat(resultado.getEquipoVisitante()).isEqualTo(visitante);
        assertThat(resultado.getTorneo()).isEqualTo(torneo);
        verify(partidoRepository).save(any(Partido.class));
    }

    @Test
    @DisplayName("crearPartido - lanza excepcion cuando equipo local y visitante son el mismo")
    void crearPartido_mismoEquipo_lanzaIllegalArgumentException() {
        when(torneoRepository.findById(1)).thenReturn(Optional.of(torneo));
        when(equipoRepository.findById(1)).thenReturn(Optional.of(local));

        assertThatThrownBy(() -> partidoService.crearPartido(1L, 1L, 1L, fecha, "Cancha Norte"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("no pueden ser el mismo");
    }

    @Test
    @DisplayName("crearPartido - lanza excepcion cuando el torneo no existe")
    void crearPartido_torneoNoExiste_lanzaRuntimeException() {
        when(torneoRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> partidoService.crearPartido(99L, 1L, 2L, fecha, "Cancha Norte"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Torneo no encontrado");
    }

    @Test
    @DisplayName("crearPartido - lanza excepcion cuando el equipo local no existe")
    void crearPartido_equipoLocalNoExiste_lanzaRuntimeException() {
        when(torneoRepository.findById(1)).thenReturn(Optional.of(torneo));
        when(equipoRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> partidoService.crearPartido(1L, 99L, 2L, fecha, "Cancha Norte"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Equipo local no encontrado");
    }

    @Test
    @DisplayName("iniciarPartido - cambia estado de PROGRAMADO a EN_CURSO")
    void iniciarPartido_estadoProgramado_cambiaAEnCurso() {
        Partido partido = new Partido(); // estado: PROGRAMADO
        partido.setId(1L);
        when(partidoRepository.findById(1L)).thenReturn(Optional.of(partido));
        when(partidoRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Partido resultado = partidoService.iniciarPartido(1L);

        assertThat(resultado.getEstado()).isEqualTo(Partido.PartidoEstado.EN_CURSO);
    }

    @Test
    @DisplayName("iniciarPartido - lanza excepcion si el partido ya esta EN_CURSO (State)")
    void iniciarPartido_yaEnCurso_lanzaIllegalStateException() {
        Partido partido = new Partido();
        partido.setId(1L);
        partido.iniciar(); // -> EN_CURSO
        when(partidoRepository.findById(1L)).thenReturn(Optional.of(partido));

        assertThatThrownBy(() -> partidoService.iniciarPartido(1L))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("iniciarPartido - lanza excepcion si el partido no existe")
    void iniciarPartido_noExiste_lanzaRuntimeException() {
        when(partidoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> partidoService.iniciarPartido(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Partido no encontrado");
    }


    @Test
    @DisplayName("registrarResultado - actualiza marcador correctamente cuando esta EN_CURSO")
    void registrarResultado_enCurso_actualizaMarcador() {
        Partido partido = new Partido();
        partido.setId(1L);
        partido.iniciar(); // -> EN_CURSO
        when(partidoRepository.findById(1L)).thenReturn(Optional.of(partido));
        when(partidoRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Partido resultado = partidoService.registrarResultado(1L, 2, 1);

        assertThat(resultado.getMarcadorLocal()).isEqualTo(2);
        assertThat(resultado.getMarcadorVisitante()).isEqualTo(1);
    }

    @Test
    @DisplayName("registrarResultado - lanza excepcion si el partido esta PROGRAMADO (no iniciado)")
    void registrarResultado_estadoProgramado_lanzaIllegalStateException() {
        Partido partido = new Partido(); // PROGRAMADO
        partido.setId(1L);
        when(partidoRepository.findById(1L)).thenReturn(Optional.of(partido));

        assertThatThrownBy(() -> partidoService.registrarResultado(1L, 1, 0))
                .isInstanceOf(IllegalStateException.class);
    }


    @Test
    @DisplayName("finalizarPartido - cambia estado de EN_CURSO a FINALIZADO")
    void finalizarPartido_enCurso_cambiaAFinalizado() {
        Partido partido = new Partido();
        partido.setId(1L);
        partido.iniciar(); // -> EN_CURSO
        when(partidoRepository.findById(1L)).thenReturn(Optional.of(partido));
        when(partidoRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Partido resultado = partidoService.finalizarPartido(1L);

        assertThat(resultado.getEstado()).isEqualTo(Partido.PartidoEstado.FINALIZADO);
    }

    @Test
    @DisplayName("finalizarPartido - lanza excepcion si el partido ya esta FINALIZADO (State)")
    void finalizarPartido_yaFinalizado_lanzaIllegalStateException() {
        Partido partido = new Partido();
        partido.setId(1L);
        partido.iniciar();   // -> EN_CURSO
        partido.finalizar(); // -> FINALIZADO
        when(partidoRepository.findById(1L)).thenReturn(Optional.of(partido));

        assertThatThrownBy(() -> partidoService.finalizarPartido(1L))
                .isInstanceOf(IllegalStateException.class);
    }


    @Test
    @DisplayName("registrarGoleador - agrega gol y suma al marcador local correctamente")
    void registrarGoleador_jugadorEquipoLocal_sumaMarcadorLocal() {
        Partido partido = new Partido();
        partido.setId(1L);
        partido.setEquipoLocal(local);
        partido.setEquipoVisitante(visitante);
        partido.iniciar(); // -> EN_CURSO
        when(partidoRepository.findById(1L)).thenReturn(Optional.of(partido));
        when(jugadorRepository.findById(10L)).thenReturn(Optional.of(jugador));
        when(partidoRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Partido resultado = partidoService.registrarGoleador(1L, 10L, 35);

        assertThat(resultado.getGoles()).hasSize(1);
        assertThat(resultado.getMarcadorLocal()).isEqualTo(1);
        assertThat(resultado.getMarcadorVisitante()).isEqualTo(0);
    }

    @Test
    @DisplayName("registrarGoleador - lanza excepcion si el partido no esta EN_CURSO")
    void registrarGoleador_noenCurso_lanzaIllegalStateException() {
        Partido partido = new Partido(); // PROGRAMADO
        partido.setId(1L);
        when(partidoRepository.findById(1L)).thenReturn(Optional.of(partido));

        assertThatThrownBy(() -> partidoService.registrarGoleador(1L, 10L, 35))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("EN_CURSO");
    }

    @Test
    @DisplayName("registrarGoleador - lanza excepcion si el jugador no existe")
    void registrarGoleador_jugadorNoExiste_lanzaRuntimeException() {
        Partido partido = new Partido();
        partido.setId(1L);
        partido.iniciar();
        when(partidoRepository.findById(1L)).thenReturn(Optional.of(partido));
        when(jugadorRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> partidoService.registrarGoleador(1L, 99L, 35))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Jugador no encontrado");
    }


    @Test
    @DisplayName("registrarTarjeta - agrega tarjeta AMARILLA correctamente")
    void registrarTarjeta_tarjetaAmarilla_agregaATarjetas() {
        Partido partido = new Partido();
        partido.setId(1L);
        partido.setEquipoLocal(local);
        partido.setEquipoVisitante(visitante);
        partido.iniciar();
        when(partidoRepository.findById(1L)).thenReturn(Optional.of(partido));
        when(jugadorRepository.findById(10L)).thenReturn(Optional.of(jugador));
        when(partidoRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Partido resultado = partidoService.registrarTarjeta(1L, 10L, Partido.Tarjeta.TipoTarjeta.AMARILLA, 60);

        assertThat(resultado.getTarjetas()).hasSize(1);
        assertThat(resultado.getTarjetas().get(0).getTipo())
                .isEqualTo(Partido.Tarjeta.TipoTarjeta.AMARILLA);
    }

    @Test
    @DisplayName("registrarTarjeta - lanza excepcion si el partido no esta EN_CURSO")
    void registrarTarjeta_noEnCurso_lanzaIllegalStateException() {
        Partido partido = new Partido(); // PROGRAMADO
        partido.setId(1L);
        when(partidoRepository.findById(1L)).thenReturn(Optional.of(partido));

        assertThatThrownBy(() -> partidoService.registrarTarjeta(1L, 10L, Partido.Tarjeta.TipoTarjeta.ROJA, 55))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("EN_CURSO");
    }

    @Test
    @DisplayName("consultarPartido - retorna el partido cuando existe")
    void consultarPartido_existe_retornaPartido() {
        Partido partido = new Partido();
        partido.setId(1L);
        when(partidoRepository.findById(1L)).thenReturn(Optional.of(partido));

        Partido resultado = partidoService.consultarPartido(1L);

        assertThat(resultado.getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("consultarPartidosPorTorneo - delega en repositorio y retorna lista")
    void consultarPartidosPorTorneo_retornaListaCorrecta() {
        Partido p1 = new Partido(); p1.setId(1L);
        Partido p2 = new Partido(); p2.setId(2L);
        when(partidoRepository.findByTorneoId(1L)).thenReturn(List.of(p1, p2));

        List<Partido> resultado = partidoService.consultarPartidosPorTorneo(1L);

        assertThat(resultado).hasSize(2);
    }

    @Test
    @DisplayName("consultarPartidosPorEquipo - delega en repositorio con mismo id para local y visitante")
    void consultarPartidosPorEquipo_retornaPartidosDelEquipo() {
        Partido p1 = new Partido(); p1.setId(1L);
        when(partidoRepository.findByEquipoLocalIdOrEquipoVisitanteId(1L, 1L)).thenReturn(List.of(p1));

        List<Partido> resultado = partidoService.consultarPartidosPorEquipo(1L);

        assertThat(resultado).hasSize(1);
    }
}
