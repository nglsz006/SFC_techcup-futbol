package edu.dosw.project.SFC_TechUp_Futbol.core.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.state.*;
import java.time.LocalDate;

@JsonIgnoreProperties({"state"})
public class Pago {

    public enum PagoEstado { PENDIENTE, EN_REVISION, APROBADO, RECHAZADO }
    public enum MedioPago { NEQUI }

    private static final double MONTO_INSCRIPCION = 130000.0;

    private String id;
    @JsonIgnore
    private String comprobante;
    private LocalDate fechaSubida;
    private PagoEstado estado;
    private MedioPago medioPago;
    private double monto;
    @JsonIgnore
    private Equipo equipo;
    private String equipoNombre;
    private PagoState state;

    public Pago() {
        this.estado = PagoEstado.PENDIENTE;
        this.state = new PendienteState();
        this.fechaSubida = LocalDate.now();
        this.monto = MONTO_INSCRIPCION;
        this.medioPago = MedioPago.NEQUI;
    }

    public void avanzar() { state.avanzar(this); }
    public void rechazar() { state.rechazar(this); }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getComprobante() { return comprobante; }
    public void setComprobante(String comprobante) { this.comprobante = comprobante; }

    public LocalDate getFechaSubida() { return fechaSubida; }
    public void setFechaSubida(LocalDate fechaSubida) { this.fechaSubida = fechaSubida; }

    public PagoEstado getEstado() { return estado; }
    public void setEstado(PagoEstado estado) { this.estado = estado; }

    public Equipo getEquipo() { return equipo; }
    public void setEquipo(Equipo equipo) {
        this.equipo = equipo;
        this.equipoNombre = equipo != null ? equipo.getNombre() : null;
    }

    public String getEquipoNombre() { return equipoNombre; }

    public PagoState getState() { return state; }
    public void setState(PagoState state) { this.state = state; }

    public MedioPago getMedioPago() { return medioPago; }
    public void setMedioPago(MedioPago medioPago) { this.medioPago = medioPago; }

    public double getMonto() { return monto; }
    public void setMonto(double monto) { this.monto = monto; }
}

