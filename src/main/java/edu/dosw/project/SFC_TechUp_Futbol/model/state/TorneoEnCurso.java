package edu.dosw.project.SFC_TechUp_Futbol.model.state;

import edu.dosw.project.SFC_TechUp_Futbol.model.EstadoTorneo;
import edu.dosw.project.SFC_TechUp_Futbol.model.Torneo;

public class TorneoEnCurso implements EstadoTorneoInterface {
    
    @Override
    public EstadoTorneoInterface iniciar(Torneo torneo) {
        return this;
    }
    
    @Override
    public EstadoTorneoInterface finalizar(Torneo torneo) {
        torneo.setEstado(EstadoTorneo.FINALIZADO);
        return new TorneoFinalizado();
    }
    
    @Override
    public boolean puedeInscribirEquipos() {
        return false;
    }
}
