package edu.dosw.project.SFC_TechUp_Futbol.core.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.state.PartidoState;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.state.ProgramadoState;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "partidos")
@JsonIgnoreProperties({"state"})
public class Partido {

    public enum PartidoEstado { PROGRAMADO, EN_CURSO, FINALIZADO }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private LocalDateTime fecha;

    @Column
    private String cancha;

    @Column(name = "marcador_local")
    private int marcadorLocal = 0;

    @Column(name = "marcador_visitante")
    private int marcadorVisitante = 0;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PartidoEstado estado;

    @ManyToOne
    @JoinColumn(name = "torneo_id")
    private Torneo torneo;

    @ManyToOne
    @JoinColumn(name = "equipo_local_id")
    private Equipo equipoLocal;

    @ManyToOne
    @JoinColumn(name = "equipo_visitante_id")
    private Equipo equipoVisitante;

    @OneToMany(mappedBy = "partido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Gol> goles = new ArrayList<>();

    @OneToMany(mappedBy = "partido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Tarjeta> tarjetas = new ArrayList<>();

    @Transient
    private PartidoState state;

    public Partido() {
        this.estado = PartidoEstado.PROGRAMADO;
        this.state = new ProgramadoState();
    }

    @PostLoad
    public void reconstruirEstado() {
        this.state = switch (this.estado) {
            case PROGRAMADO -> new ProgramadoState();
            case EN_CURSO -> new edu.dosw.project.SFC_TechUp_Futbol.core.model.state.EnCursoState();
            case FINALIZADO -> new edu.dosw.project.SFC_TechUp_Futbol.core.model.state.FinalizadoPartidoState();
        };
    }

    public void iniciar() { state.iniciar(this); }
    public void registrarResultado(int golesLocal, int golesVisitante) { state.registrarResultado(this, golesLocal, golesVisitante); }
    public void finalizar() { state.finalizar(this); }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }

    public String getCancha() { return cancha; }
    public void setCancha(String cancha) { this.cancha = cancha; }

    public int getMarcadorLocal() { return marcadorLocal; }
    public void setMarcadorLocal(int marcadorLocal) { this.marcadorLocal = marcadorLocal; }

    public int getMarcadorVisitante() { return marcadorVisitante; }
    public void setMarcadorVisitante(int marcadorVisitante) { this.marcadorVisitante = marcadorVisitante; }

    public PartidoEstado getEstado() { return estado; }
    public void setEstado(PartidoEstado estado) { this.estado = estado; }

    public Torneo getTorneo() { return torneo; }
    public void setTorneo(Torneo torneo) { this.torneo = torneo; }

    public Equipo getEquipoLocal() { return equipoLocal; }
    public void setEquipoLocal(Equipo equipoLocal) { this.equipoLocal = equipoLocal; }

    public Equipo getEquipoVisitante() { return equipoVisitante; }
    public void setEquipoVisitante(Equipo equipoVisitante) { this.equipoVisitante = equipoVisitante; }

    public List<Gol> getGoles() { return goles; }
    public void setGoles(List<Gol> goles) { this.goles = goles; }

    public List<Tarjeta> getTarjetas() { return tarjetas; }
    public void setTarjetas(List<Tarjeta> tarjetas) { this.tarjetas = tarjetas; }

    public PartidoState getState() { return state; }
    public void setState(PartidoState state) { this.state = state; }


    // ── Clases internas ────────────────────────────────────────────────────────

    @Entity
    @Table(name = "goles")
    public static class Gol {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column
        private int minuto;

        @ManyToOne
        @JoinColumn(name = "jugador_id")
        private Jugador jugador;

        @ManyToOne
        @JoinColumn(name = "partido_id")
        private Partido partido;

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public int getMinuto() { return minuto; }
        public void setMinuto(int minuto) { this.minuto = minuto; }

        public Jugador getJugador() { return jugador; }
        public void setJugador(Jugador jugador) { this.jugador = jugador; }

        public Partido getPartido() { return partido; }
        public void setPartido(Partido partido) { this.partido = partido; }
    }

    @Entity
    @Table(name = "tarjetas")
    public static class Tarjeta {

        public enum TipoTarjeta { AMARILLA, ROJA }

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Enumerated(EnumType.STRING)
        @Column
        private TipoTarjeta tipo;

        @Column
        private int minuto;

        @ManyToOne
        @JoinColumn(name = "jugador_id")
        private Jugador jugador;

        @ManyToOne
        @JoinColumn(name = "partido_id")
        private Partido partido;

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public TipoTarjeta getTipo() { return tipo; }
        public void setTipo(TipoTarjeta tipo) { this.tipo = tipo; }

        public int getMinuto() { return minuto; }
        public void setMinuto(int minuto) { this.minuto = minuto; }

        public Jugador getJugador() { return jugador; }
        public void setJugador(Jugador jugador) { this.jugador = jugador; }

        public Partido getPartido() { return partido; }
        public void setPartido(Partido partido) { this.partido = partido; }
    }
}
