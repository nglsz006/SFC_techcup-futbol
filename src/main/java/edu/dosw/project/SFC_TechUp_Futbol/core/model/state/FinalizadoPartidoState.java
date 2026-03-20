package edu.dosw.project.SFC_TechUp_Futbol.core.model.state;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Partido;

public class FinalizadoPartidoState implements PartidoState {

    @Override
    public void iniciar(Partido partido) {
        throw new IllegalStateException("El partido ya finalizo, no puede iniciarse de nuevo.");
    }

    @Override
    public void registrarResultado(Partido partido, int golesLocal, int golesVisitante) {
        throw new IllegalStateException("El partido ya finalizo, no se puede modificar el resultado.");
    }

    @Override
    public void finalizar(Partido partido) {
        throw new IllegalStateException("El partido ya esta finalizado.");
    }

    @Override
    public String getNombre() { return "FINALIZADO"; }
}

