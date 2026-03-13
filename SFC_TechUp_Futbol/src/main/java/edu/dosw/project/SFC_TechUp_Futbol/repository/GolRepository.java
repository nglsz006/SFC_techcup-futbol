package edu.dosw.project.SFC_TechUp_Futbol.repository;

import edu.dosw.project.SFC_TechUp_Futbol.model.Gol;

import java.util.List;

public interface GolRepository {
    Gol save(Gol gol);
    List<Gol> findByPartidoId(Long partidoId);
    List<Gol> findByJugadorId(Long jugadorId);
}
