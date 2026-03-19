package edu.dosw.project.SFC_TechUp_Futbol.core.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.state.*;
import java.time.LocalDate;

@JsonIgnoreProperties({"state"})
public class Pago {

    public enum PagoEstado { PENDIENTE, EN_REVISION, APROBADO, RECHAZADO }

    private Long id;
    private String comprobante;
    private LocalDate fechaSubida;
    private PagoEstado estado;
    private Equipo equipo;
    private PagoState state;

    public Pago() {
        this.estado = PagoEstado.PENDIENTE;
        this.state = new PendienteState();
        this.fechaSubida = LocalDate.now();
    }

    public void avanzar() { state.avanzar(this); }
    public void rechazar() { state.rechazar(this); }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getComprobante() { return comprobante; }
    public void setComprobante(String comprobante) { this.comprobante = comprobante; }

    public LocalDate getFechaSubida() { return fechaSubida; }
    public void setFechaSubida(LocalDate fechaSubida) { this.fechaSubida = fechaSubida; }

    public PagoEstado getEstado() { return estado; }
    public void setEstado(PagoEstado estado) { this.estado = estado; }

    public Equipo getEquipo() { return equipo; }
    public void setEquipo(Equipo equipo) { this.equipo = equipo; }

    public PagoState getState() { return state; }
    public void setState(PagoState state) { this.state = state; }
}
