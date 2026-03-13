package edu.dosw.project.SFC_TechUp_Futbol.validators;

import edu.dosw.project.SFC_TechUp_Futbol.model.Jugador;
import edu.dosw.project.SFC_TechUp_Futbol.model.Posicion;

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
