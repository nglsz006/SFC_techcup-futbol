package edu.dosw.project.SFC_TechUp_Futbol.core.repository;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.PerfilDeportivo;

import java.util.Optional;

public interface PerfilDeportivoRepository {
    PerfilDeportivo save(PerfilDeportivo perfil);
    Optional<PerfilDeportivo> findById(String id);
    Optional<PerfilDeportivo> findByJugadorId(String jugadorId);
}
