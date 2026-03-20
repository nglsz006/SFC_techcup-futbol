package edu.dosw.project.SFC_TechUp_Futbol.core.model.state;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Pago;

public interface PagoState {
    void avanzar(Pago pago);
    void rechazar(Pago pago);
    String getNombre();
}

