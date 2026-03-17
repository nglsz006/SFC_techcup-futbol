package edu.dosw.project.SFC_TechUp_Futbol.model.state;

import edu.dosw.project.SFC_TechUp_Futbol.model.EstadoTorneo;
import edu.dosw.project.SFC_TechUp_Futbol.model.Torneo;

public class TorneoCreado implements EstadoTorneoInterface {
    
    @Override
    public EstadoTorneoInterface iniciar(Torneo torneo) {
        torneo.setEstado(EstadoTorneo.EN_CURSO);
        return new TorneoEnCurso();
    }
    
    @Override
    public EstadoTorneoInterface finalizar(Torneo torneo) {
        return this;
    }
    
    @Override
    public boolean puedeInscribirEquipos() {
        return true;
    }
}
