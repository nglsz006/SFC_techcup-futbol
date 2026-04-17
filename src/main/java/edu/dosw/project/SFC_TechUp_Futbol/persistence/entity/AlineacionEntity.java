package edu.dosw.project.SFC_TechUp_Futbol.persistence.entity;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Alineacion;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "alineacion")
public class AlineacionEntity {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private String id;

    @Column(name = "equipo_id", nullable = false)
    private String equipoId;

    @Column(name = "partido_id", nullable = false)
    private String partidoId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Alineacion.Formacion formacion;

    @ElementCollection
    @CollectionTable(name = "alineacion_titulares", joinColumns = @JoinColumn(name = "alineacion_id"))
    @Column(name = "jugador_id")
    private List<String> titulares = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "alineacion_reservas", joinColumns = @JoinColumn(name = "alineacion_id"))
    @Column(name = "jugador_id")
    private List<String> reservas = new ArrayList<>();

    public AlineacionEntity() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEquipoId() {
        return equipoId;
    }

    public void setEquipoId(String equipoId) {
        this.equipoId = equipoId;
    }

    public String getPartidoId() {
        return partidoId;
    }

    public void setPartidoId(String partidoId) {
        this.partidoId = partidoId;
    }

    public Alineacion.Formacion getFormacion() {
        return formacion;
    }

    public void setFormacion(Alineacion.Formacion formacion) {
        this.formacion = formacion;
    }

    public List<String> getTitulares() {
        return titulares;
    }

    public void setTitulares(List<String> titulares) {
        this.titulares = titulares;
    }

    public List<String> getReservas() {
        return reservas;
    }

    public void setReservas(List<String> reservas) {
        this.reservas = reservas;
    }
}
