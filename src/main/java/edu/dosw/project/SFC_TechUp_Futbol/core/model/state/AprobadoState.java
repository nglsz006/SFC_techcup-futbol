package edu.dosw.project.SFC_TechUp_Futbol.core.model.state;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Pago;

public class AprobadoState implements PagoState {

    @Override
    public void avanzar(Pago pago) {
        throw new IllegalStateException("El pago ya fue aprobado, no puede avanzar mas.");
    }

    @Override
    public void rechazar(Pago pago) {
        throw new IllegalStateException("No se puede rechazar un pago que ya fue aprobado.");
    }

    @Override
    public String getNombre() { return "APROBADO"; }
}

