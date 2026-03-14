package edu.dosw.project.SFC_TechUp_Futbol.core.validator;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Jugador;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.Posicion;

public class JugadorValidator {
    public boolean dorsalValido(int dorsal) {
        return dorsal > 0;
    }
    public boolean posicionValida(Posicion posicion) {
        return posicion != null;
    }
    public boolean jugadorDisponibleParaEquipo(Jugador jugador) {
        return jugador.isAvailable();
    }
}

