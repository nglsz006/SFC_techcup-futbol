package edu.dosw.project.SFC_TechUp_Futbol.core.repository;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Gol;

import java.util.List;

public interface GolRepository {
    Gol save(Gol gol);
    List<Gol> findByPartidoId(Long partidoId);
    List<Gol> findByJugadorId(Long jugadorId);
}

