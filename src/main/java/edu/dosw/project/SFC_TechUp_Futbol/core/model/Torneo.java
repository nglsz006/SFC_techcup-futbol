package edu.dosw.project.SFC_TechUp_Futbol.core.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.state.EstadoTorneoInterface;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.state.TorneoCreado;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "torneos")
@JsonIgnoreProperties({"estadoObj"})
public class Torneo {

    public enum EstadoTorneo { CREADO, EN_CURSO, FINALIZADO }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String nombre;

    @Column(name = "fecha_inicio")
    private LocalDateTime fechaInicio;

    @Column(name = "fecha_fin")
    private LocalDateTime fechaFin;

    @Column(name = "cantidad_equipos")
    private int cantidadEquipos;

    @Column
    private double costo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoTorneo estado;

    @Column(columnDefinition = "TEXT")
    private String reglamento;

    @Column(name = "cierre_inscripciones")
    private LocalDateTime cierreInscripciones;

    @Column
    private String canchas;

    @Column
    private String horarios;

    @Column(columnDefinition = "TEXT")
    private String sanciones;

    @Transient
    private EstadoTorneoInterface estadoObj;

    public Torneo() {
        this.estado = EstadoTorneo.CREADO;
        this.estadoObj = new TorneoCreado();
    }

    public Torneo(int id, String nombre, LocalDateTime fechaInicio, LocalDateTime fechaFin,
                  int cantidadEquipos, double costo) {
        this.id = id;
        this.nombre = nombre;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.cantidadEquipos = cantidadEquipos;
        this.costo = costo;
        this.estado = EstadoTorneo.CREADO;
        this.estadoObj = new TorneoCreado();
    }

    @PostLoad
    public void reconstruirEstado() {
        this.estadoObj = switch (this.estado) {
            case CREADO -> new TorneoCreado();
            case EN_CURSO -> new edu.dosw.project.SFC_TechUp_Futbol.core.model.state.TorneoEnCurso();
            case FINALIZADO -> new edu.dosw.project.SFC_TechUp_Futbol.core.model.state.TorneoFinalizado();
        };
    }

    public void iniciar() { estadoObj = estadoObj.iniciar(this); }
    public void finalizar() { estadoObj = estadoObj.finalizar(this); }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public LocalDateTime getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDateTime fechaInicio) { this.fechaInicio = fechaInicio; }

    public LocalDateTime getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDateTime fechaFin) { this.fechaFin = fechaFin; }

    public int getCantidadEquipos() { return cantidadEquipos; }
    public void setCantidadEquipos(int cantidadEquipos) { this.cantidadEquipos = cantidadEquipos; }

    public double getCosto() { return costo; }
    public void setCosto(double costo) { this.costo = costo; }

    public EstadoTorneo getEstado() { return estado; }
    public void setEstado(EstadoTorneo estado) { this.estado = estado; }

    public String getReglamento() { return reglamento; }
    public void setReglamento(String reglamento) { this.reglamento = reglamento; }

    public LocalDateTime getCierreInscripciones() { return cierreInscripciones; }
    public void setCierreInscripciones(LocalDateTime cierreInscripciones) { this.cierreInscripciones = cierreInscripciones; }

    public String getCanchas() { return canchas; }
    public void setCanchas(String canchas) { this.canchas = canchas; }

    public String getHorarios() { return horarios; }
    public void setHorarios(String horarios) { this.horarios = horarios; }

    public String getSanciones() { return sanciones; }
    public void setSanciones(String sanciones) { this.sanciones = sanciones; }

    public EstadoTorneoInterface getEstadoObj() { return estadoObj; }
    public void setEstadoObj(EstadoTorneoInterface estadoObj) { this.estadoObj = estadoObj; }
}
