package edu.dosw.project.SFC_TechUp_Futbol.persistence.repository;

import edu.dosw.project.SFC_TechUp_Futbol.persistence.entity.PerfilDeportivoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PerfilDeportivoJpaRepository extends JpaRepository<PerfilDeportivoEntity, Long> {
    Optional<PerfilDeportivoEntity> findByJugadorId(Long jugadorId);
}
