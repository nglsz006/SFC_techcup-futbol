package edu.dosw.project.SFC_TechUp_Futbol.core.model.state;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Partido;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.PartidoEstado;

public class ProgramadoState implements PartidoState {

    @Override
    public void iniciar(Partido partido) {
        partido.setEstado(PartidoEstado.EN_CURSO);
        partido.setState(new EnCursoState());
    }

    @Override
    public void registrarResultado(Partido partido, int golesLocal, int golesVisitante) {
        throw new IllegalStateException("No se puede registrar resultado: el partido aún no ha iniciado.");
    }

    @Override
    public void finalizar(Partido partido) {
        throw new IllegalStateException("No se puede finalizar un partido que aún no ha iniciado.");
    }

    @Override
    public String getNombre() { return "PROGRAMADO"; }
}

