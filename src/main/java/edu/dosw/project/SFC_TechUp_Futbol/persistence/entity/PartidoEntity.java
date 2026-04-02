package edu.dosw.project.SFC_TechUp_Futbol.persistence.entity;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Partido;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "partido")
public class PartidoEntity {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private String id;

    @Column
    private LocalDateTime fecha;

    @Column
    private String cancha;

    @Column(name = "marcador_local")
    private int marcadorLocal;

    @Column(name = "marcador_visitante")
    private int marcadorVisitante;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Partido.PartidoEstado estado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "torneo_id")
    private TorneoEntity torneo;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipo_local_id", nullable = false)
    private EquipoEntity equipoLocal;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipo_visitante_id", nullable = false)
    private EquipoEntity equipoVisitante;

    @OneToMany(mappedBy = "partido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GolEntity> goles = new ArrayList<>();

    @OneToMany(mappedBy = "partido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TarjetaEntity> tarjetas = new ArrayList<>();

    public PartidoEntity() {
        this.estado = Partido.PartidoEstado.PROGRAMADO;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public String getCancha() {
        return cancha;
    }

    public void setCancha(String cancha) {
        this.cancha = cancha;
    }

    public int getMarcadorLocal() {
        return marcadorLocal;
    }

    public void setMarcadorLocal(int marcadorLocal) {
        this.marcadorLocal = marcadorLocal;
    }

    public int getMarcadorVisitante() {
        return marcadorVisitante;
    }

    public void setMarcadorVisitante(int marcadorVisitante) {
        this.marcadorVisitante = marcadorVisitante;
    }

    public Partido.PartidoEstado getEstado() {
        return estado;
    }

    public void setEstado(Partido.PartidoEstado estado) {
        this.estado = estado;
    }

    public TorneoEntity getTorneo() {
        return torneo;
    }

    public void setTorneo(TorneoEntity torneo) {
        this.torneo = torneo;
    }

    public EquipoEntity getEquipoLocal() {
        return equipoLocal;
    }

    public void setEquipoLocal(EquipoEntity equipoLocal) {
        this.equipoLocal = equipoLocal;
    }

    public EquipoEntity getEquipoVisitante() {
        return equipoVisitante;
    }

    public void setEquipoVisitante(EquipoEntity equipoVisitante) {
        this.equipoVisitante = equipoVisitante;
    }

    public List<GolEntity> getGoles() {
        return goles;
    }

    public void setGoles(List<GolEntity> goles) {
        this.goles = goles;
    }

    public List<TarjetaEntity> getTarjetas() {
        return tarjetas;
    }

    public void setTarjetas(List<TarjetaEntity> tarjetas) {
        this.tarjetas = tarjetas;
    }
}
