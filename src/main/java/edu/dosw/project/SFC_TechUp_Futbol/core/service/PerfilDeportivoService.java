package edu.dosw.project.SFC_TechUp_Futbol.core.service;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Jugador;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.PerfilDeportivo;

import java.util.List;

public interface PerfilDeportivoService {
    PerfilDeportivo crearPerfil(String jugadorId, List<Jugador.Posicion> posiciones, int dorsal,
                                String foto, int edad, PerfilDeportivo.Genero genero,
                                String identificacion, Integer semestre);

    PerfilDeportivo editarPerfil(String jugadorId, List<Jugador.Posicion> posiciones, int dorsal,
                                 String foto, int edad, PerfilDeportivo.Genero genero,
                                 String identificacion, Integer semestre);

    PerfilDeportivo consultarPerfil(String jugadorId);
}
