package edu.dosw.project.SFC_TechUp_Futbol.persistence.repository;

import edu.dosw.project.SFC_TechUp_Futbol.persistence.entity.AlineacionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlineacionJpaRepository extends JpaRepository<AlineacionEntity, Long> {
}
