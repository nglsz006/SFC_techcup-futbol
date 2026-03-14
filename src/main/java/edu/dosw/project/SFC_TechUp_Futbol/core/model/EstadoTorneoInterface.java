package edu.dosw.project.SFC_TechUp_Futbol.core.model;

public interface EstadoTorneoInterface {
    EstadoTorneoInterface iniciar(Torneo torneo);
    EstadoTorneoInterface finalizar(Torneo torneo);
    boolean puedeInscribirEquipos();
}

