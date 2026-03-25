package edu.dosw.project.SFC_TechUp_Futbol.core.service;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Jugador;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.PerfilDeportivo;

import java.util.List;

public interface PerfilDeportivoService {
    PerfilDeportivo crearPerfil(Long jugadorId, List<Jugador.Posicion> posiciones, int dorsal,
                                String foto, int edad, PerfilDeportivo.Genero genero,
                                String identificacion, Integer semestre);

    PerfilDeportivo editarPerfil(Long jugadorId, List<Jugador.Posicion> posiciones, int dorsal,
                                 String foto, int edad, PerfilDeportivo.Genero genero,
                                 String identificacion, Integer semestre);

    PerfilDeportivo consultarPerfil(Long jugadorId);
}
