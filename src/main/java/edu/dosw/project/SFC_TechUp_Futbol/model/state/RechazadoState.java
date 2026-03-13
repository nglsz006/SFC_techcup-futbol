package edu.dosw.project.SFC_TechUp_Futbol.model.state;

import edu.dosw.project.SFC_TechUp_Futbol.model.Pago;

public class RechazadoState implements PagoState {

    @Override
    public void avanzar(Pago pago) {
        throw new IllegalStateException("El pago fue rechazado y no puede ser aprobado directamente.");
    }

    @Override
    public void rechazar(Pago pago) {
        throw new IllegalStateException("El pago ya está rechazado.");
    }

    @Override
    public String getNombre() { return "RECHAZADO"; }
}
