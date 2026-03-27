package edu.dosw.project.SFC_TechUp_Futbol.persistence.entity;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Torneo;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "torneo")
public class TorneoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
    private Torneo.EstadoTorneo estado;

    @Column(columnDefinition = "TEXT")
    private String reglamento;

    @Column(name = "cierre_inscripciones")
    private LocalDateTime cierreInscripciones;

    @Column
    private String canchas;

    @Column
    private String horarios;

    @Column
    private String sanciones;

    public TorneoEntity() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public LocalDateTime getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDateTime fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDateTime getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDateTime fechaFin) {
        this.fechaFin = fechaFin;
    }

    public int getCantidadEquipos() {
        return cantidadEquipos;
    }

    public void setCantidadEquipos(int cantidadEquipos) {
        this.cantidadEquipos = cantidadEquipos;
    }

    public double getCosto() {
        return costo;
    }

    public void setCosto(double costo) {
        this.costo = costo;
    }

    public Torneo.EstadoTorneo getEstado() {
        return estado;
    }

    public void setEstado(Torneo.EstadoTorneo estado) {
        this.estado = estado;
    }

    public String getReglamento() {
        return reglamento;
    }

    public void setReglamento(String reglamento) {
        this.reglamento = reglamento;
    }

    public LocalDateTime getCierreInscripciones() {
        return cierreInscripciones;
    }

    public void setCierreInscripciones(LocalDateTime cierreInscripciones) {
        this.cierreInscripciones = cierreInscripciones;
    }

    public String getCanchas() {
        return canchas;
    }

    public void setCanchas(String canchas) {
        this.canchas = canchas;
    }

    public String getHorarios() {
        return horarios;
    }

    public void setHorarios(String horarios) {
        this.horarios = horarios;
    }

    public String getSanciones() {
        return sanciones;
    }

    public void setSanciones(String sanciones) {
        this.sanciones = sanciones;
    }
}
