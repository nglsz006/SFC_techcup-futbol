package edu.dosw.project.SFC_TechUp_Futbol.core.model.state;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Pago;

public class PendienteState implements PagoState {

    @Override
    public void avanzar(Pago pago) {
        pago.setEstado(Pago.PagoEstado.EN_REVISION);
        pago.setState(new EnRevisionState());
    }

    @Override
    public void rechazar(Pago pago) {
        throw new IllegalStateException("No se puede rechazar un pago que aún no ha sido revisado.");
    }

    @Override
    public String getNombre() { return "PENDIENTE"; }
}
