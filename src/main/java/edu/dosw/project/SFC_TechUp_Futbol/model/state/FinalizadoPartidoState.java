package edu.dosw.project.SFC_TechUp_Futbol.model.state;

import edu.dosw.project.SFC_TechUp_Futbol.model.Partido;

public class FinalizadoPartidoState implements PartidoState {

    @Override
    public void iniciar(Partido partido) {
        throw new IllegalStateException("El partido ya finalizó, no puede iniciarse de nuevo.");
    }

    @Override
    public void registrarResultado(Partido partido, int golesLocal, int golesVisitante) {
        throw new IllegalStateException("El partido ya finalizó, no se puede modificar el resultado.");
    }

    @Override
    public void finalizar(Partido partido) {
        throw new IllegalStateException("El partido ya está finalizado.");
    }

    @Override
    public String getNombre() { return "FINALIZADO"; }
}
