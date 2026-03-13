package edu.dosw.project.SFC_TechUp_Futbol.model.state;

import edu.dosw.project.SFC_TechUp_Futbol.model.Pago;

public class AprobadoState implements PagoState {

    @Override
    public void avanzar(Pago pago) {
        throw new IllegalStateException("El pago ya está aprobado, no puede avanzar más.");
    }

    @Override
    public void rechazar(Pago pago) {
        throw new IllegalStateException("No se puede rechazar un pago ya aprobado.");
    }

    @Override
    public String getNombre() { return "APROBADO"; }
}
