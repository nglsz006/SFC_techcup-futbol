package edu.dosw.project.SFC_TechUp_Futbol.core.repository;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.PerfilDeportivo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PerfilDeportivoRepository extends JpaRepository<PerfilDeportivo, Long> {
    Optional<PerfilDeportivo> findByJugadorId(Long jugadorId);
}
