package edu.dosw.project.SFC_TechUp_Futbol.persistence.repository;

import edu.dosw.project.SFC_TechUp_Futbol.persistence.entity.EquipoEntity;
import org.springframework.data.jpa.repository6.JpaRepository;

public interface EquipoJpaRepository extends JpaRepository<EquipoEntity, Long> {
}
