package edu.dosw.project.SFC_TechUp_Futbol.core.model.state;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Torneo;

public class TorneoFinalizado implements EstadoTorneoInterface {

    @Override
    public EstadoTorneoInterface iniciar(Torneo torneo) {
        return this;
    }

    @Override
    public EstadoTorneoInterface finalizar(Torneo torneo) {
        return this;
    }

    @Override
    public boolean puedeInscribirEquipos() {
        return false;
    }
}
