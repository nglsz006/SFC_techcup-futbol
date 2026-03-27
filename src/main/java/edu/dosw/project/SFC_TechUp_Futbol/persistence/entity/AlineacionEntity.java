package edu.dosw.project.SFC_TechUp_Futbol.persistence.entity;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Alineacion;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "alineacion")
public class AlineacionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "equipo_id", nullable = false)
    private Long equipoId;

    @Column(name = "partido_id", nullable = false)
    private Long partidoId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Alineacion.Formacion formacion;

    @ElementCollection
    @CollectionTable(name = "alineacion_titulares", joinColumns = @JoinColumn(name = "alineacion_id"))
    @Column(name = "jugador_id")
    private List<Integer> titulares = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "alineacion_reservas", joinColumns = @JoinColumn(name = "alineacion_id"))
    @Column(name = "jugador_id")
    private List<Integer> reservas = new ArrayList<>();

    public AlineacionEntity() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEquipoId() {
        return equipoId;
    }

    public void setEquipoId(Long equipoId) {
        this.equipoId = equipoId;
    }

    public Long getPartidoId() {
        return partidoId;
    }

    public void setPartidoId(Long partidoId) {
        this.partidoId = partidoId;
    }

    public Alineacion.Formacion getFormacion() {
        return formacion;
    }

    public void setFormacion(Alineacion.Formacion formacion) {
        this.formacion = formacion;
    }

    public List<Integer> getTitulares() {
        return titulares;
    }

    public void setTitulares(List<Integer> titulares) {
        this.titulares = titulares;
    }

    public List<Integer> getReservas() {
        return reservas;
    }

    public void setReservas(List<Integer> reservas) {
        this.reservas = reservas;
    }
}
