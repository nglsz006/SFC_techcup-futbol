package edu.dosw.project.SFC_TechUp_Futbol.model.state;

import edu.dosw.project.SFC_TechUp_Futbol.model.Partido;
import edu.dosw.project.SFC_TechUp_Futbol.model.PartidoEstado;

public class EnCursoState implements PartidoState {

    @Override
    public void iniciar(Partido partido) {
        throw new IllegalStateException("El partido ya está en curso.");
    }

    @Override
    public void registrarResultado(Partido partido, int golesLocal, int golesVisitante) {
        partido.setMarcadorLocal(golesLocal);
        partido.setMarcadorVisitante(golesVisitante);
    }

    @Override
    public void finalizar(Partido partido) {
        partido.setEstado(PartidoEstado.FINALIZADO);
        partido.setState(new FinalizadoPartidoState());
    }

    @Override
    public String getNombre() { return "EN_CURSO"; }
}
