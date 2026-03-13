package edu.dosw.project.SFC_TechUp_Futbol.repository;

import edu.dosw.project.SFC_TechUp_Futbol.model.Equipo;

import java.util.Optional;

public interface EquipoRepository {
    Optional<Equipo> findById(Long id);
}
