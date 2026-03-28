package edu.dosw.project.SFC_TechUp_Futbol.core.repository;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Sancion;

import java.util.List;
import java.util.Optional;

public interface SancionRepository {
    Sancion save(Sancion sancion);
    Optional<Sancion> findById(Long id);
    List<Sancion> findAll();
    List<Sancion> findByJugadorId(Long jugadorId);
}
