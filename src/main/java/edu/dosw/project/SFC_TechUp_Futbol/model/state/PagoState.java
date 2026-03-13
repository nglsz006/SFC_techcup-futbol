package edu.dosw.project.SFC_TechUp_Futbol.model.state;

import edu.dosw.project.SFC_TechUp_Futbol.model.Pago;

public interface PagoState {
    void avanzar(Pago pago);
    void rechazar(Pago pago);
    String getNombre();
}
