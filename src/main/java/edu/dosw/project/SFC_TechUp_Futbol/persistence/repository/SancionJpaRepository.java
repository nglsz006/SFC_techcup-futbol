package edu.dosw.project.SFC_TechUp_Futbol.persistence.repository;

import edu.dosw.project.SFC_TechUp_Futbol.persistence.entity.SancionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SancionJpaRepository extends JpaRepository<SancionEntity, Long> {
    List<SancionEntity> findByJugadorId(Long jugadorId);
}
