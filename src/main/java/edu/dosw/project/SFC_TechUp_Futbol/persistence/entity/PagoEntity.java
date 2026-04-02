package edu.dosw.project.SFC_TechUp_Futbol.persistence.entity;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Pago;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "pago")
public class PagoEntity {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private String id;

    @NotNull
    @Column(nullable = false)
    private String comprobante;

    @Column(name = "fecha_subida")
    private LocalDate fechaSubida;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Pago.PagoEstado estado;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipo_id", nullable = false)
    private EquipoEntity equipo;

    public PagoEntity() {
        this.estado = Pago.PagoEstado.PENDIENTE;
        this.fechaSubida = LocalDate.now();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getComprobante() {
        return comprobante;
    }

    public void setComprobante(String comprobante) {
        this.comprobante = comprobante;
    }

    public LocalDate getFechaSubida() {
        return fechaSubida;
    }

    public void setFechaSubida(LocalDate fechaSubida) {
        this.fechaSubida = fechaSubida;
    }

    public Pago.PagoEstado getEstado() {
        return estado;
    }

    public void setEstado(Pago.PagoEstado estado) {
        this.estado = estado;
    }

    public EquipoEntity getEquipo() {
        return equipo;
    }

    public void setEquipo(EquipoEntity equipo) {
        this.equipo = equipo;
    }
}
