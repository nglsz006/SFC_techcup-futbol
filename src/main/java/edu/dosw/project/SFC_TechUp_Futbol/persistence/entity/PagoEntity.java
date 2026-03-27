package edu.dosw.project.SFC_TechUp_Futbol.persistence.entity;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Pago;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "pago")
public class PagoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String comprobante;

    @Column(name = "fecha_subida")
    private LocalDate fechaSubida;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Pago.PagoEstado estado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipo_id")
    private EquipoEntity equipo;

    public PagoEntity() {
        this.estado = Pago.PagoEstado.PENDIENTE;
        this.fechaSubida = LocalDate.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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
