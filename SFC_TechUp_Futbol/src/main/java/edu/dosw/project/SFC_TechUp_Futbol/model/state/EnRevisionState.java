package edu.dosw.project.SFC_TechUp_Futbol.model.state;

import edu.dosw.project.SFC_TechUp_Futbol.model.Pago;
import edu.dosw.project.SFC_TechUp_Futbol.model.PagoEstado;

public class EnRevisionState implements PagoState {

    @Override
    public void avanzar(Pago pago) {
        pago.setEstado(PagoEstado.APROBADO);
        pago.setState(new AprobadoState());
    }

    @Override
    public void rechazar(Pago pago) {
        pago.setEstado(PagoEstado.RECHAZADO);
        pago.setState(new RechazadoState());
    }

    @Override
    public String getNombre() { return "EN_REVISION"; }
}
