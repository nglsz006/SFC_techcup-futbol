package edu.dosw.project.SFC_TechUp_Futbol.model.state;

import edu.dosw.project.SFC_TechUp_Futbol.model.Torneo;

public interface EstadoTorneoInterface {
    EstadoTorneoInterface iniciar(Torneo torneo);
    EstadoTorneoInterface finalizar(Torneo torneo);
    boolean puedeInscribirEquipos();
}
