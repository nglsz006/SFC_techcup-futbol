package edu.dosw.project.SFC_TechUp_Futbol.core.model.state;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Pago;

public class RechazadoState implements PagoState {

    @Override
    public void avanzar(Pago pago) {
        throw new IllegalStateException("El pago fue rechazado y no puede aprobarse directamente.");
    }

    @Override
    public void rechazar(Pago pago) {
        throw new IllegalStateException("El pago ya fue rechazado.");
    }

    @Override
    public String getNombre() { return "RECHAZADO"; }
}

