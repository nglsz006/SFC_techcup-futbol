package edu.dosw.project.SFC_TechUp_Futbol.repository;

import edu.dosw.project.SFC_TechUp_Futbol.model.Torneo;

import java.util.Optional;

public interface TorneoRepository {
    Optional<Torneo> findById(Long id);
}
