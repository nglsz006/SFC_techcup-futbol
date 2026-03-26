package edu.dosw.project.SFC_TechUp_Futbol.core.repository;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.PerfilDeportivo;

import java.util.Optional;

public interface PerfilDeportivoRepository {
    PerfilDeportivo save(PerfilDeportivo perfil);
    Optional<PerfilDeportivo> findById(Long id);
    Optional<PerfilDeportivo> findByJugadorId(Long jugadorId);
}
