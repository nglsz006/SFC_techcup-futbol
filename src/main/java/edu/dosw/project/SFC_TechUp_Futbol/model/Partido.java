package edu.dosw.project.SFC_TechUp_Futbol.model;

import edu.dosw.project.SFC_TechUp_Futbol.model.state.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Partido {

    private Long id;
    private LocalDateTime fecha;
    private String cancha;
    private int marcadorLocal = 0;
    private int marcadorVisitante = 0;
    private PartidoEstado estado;
    private Torneo torneo;
    private Equipo equipoLocal;
    private Equipo equipoVisitante;
    private List<Gol> goles = new ArrayList<>();
    private List<Tarjeta> tarjetas = new ArrayList<>();
    private PartidoState state;

    public Partido() {
        this.estado = PartidoEstado.PROGRAMADO;
        this.state = new ProgramadoState();
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
}
