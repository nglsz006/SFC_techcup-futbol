package edu.dosw.project.SFC_TechUp_Futbol.core.validator;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Jugador;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.PerfilDeportivo;

import java.util.List;

public class PerfilDeportivoValidator {

    public void validarPerfil(List<Jugador.Posicion> posiciones, int dorsal,
                              int edad, PerfilDeportivo.Genero genero,
                              String identificacion) {
        validarPosiciones(posiciones);
        validarDorsal(dorsal);
        validarEdad(edad);
        validarGenero(genero);
        validarIdentificacion(identificacion);
    }

    public void validarPosiciones(List<Jugador.Posicion> posiciones) {
        if (posiciones == null || posiciones.isEmpty()) {
            throw new IllegalArgumentException("Debe seleccionar al menos una posicion de juego.");
        }
    }

    public void validarDorsal(int dorsal) {
        if (dorsal <= 0) {
            throw new IllegalArgumentException("El dorsal debe ser un numero positivo.");
        }
    }

    public void validarEdad(int edad) {
        if (edad <= 0) {
            throw new IllegalArgumentException("La edad debe ser mayor a 0.");
        }
        if (edad > 100) {
            throw new IllegalArgumentException("La edad ingresada no es valida.");
        }
    }

    public void validarGenero(PerfilDeportivo.Genero genero) {
        if (genero == null) {
            throw new IllegalArgumentException("El genero es obligatorio.");
        }
    }

    public void validarIdentificacion(String identificacion) {
        if (identificacion == null || identificacion.isBlank()) {
            throw new IllegalArgumentException("La identificacion es obligatoria.");
        }
    }

    public void validarSemestre(Integer semestre) {
        if (semestre != null && semestre <= 0) {
            throw new IllegalArgumentException("El semestre debe ser un numero positivo.");
        }
    }
}
