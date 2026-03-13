package edu.dosw.project.SFC_TechUp_Futbol.model.state;

import edu.dosw.project.SFC_TechUp_Futbol.model.Partido;

public interface PartidoState {
    void iniciar(Partido partido);
    void registrarResultado(Partido partido, int golesLocal, int golesVisitante);
    void finalizar(Partido partido);
    String getNombre();
}
