package edu.dosw.project.SFC_TechUp_Futbol.core.repository;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Equipo;
import java.util.List;
import java.util.Optional;

public interface EquipoRepository {
    Equipo save(Equipo equipo);
    Optional<Equipo> findById(String id);
    List<Equipo> findAll();
    List<Equipo> findByCapitanId(String capitanId);
}

