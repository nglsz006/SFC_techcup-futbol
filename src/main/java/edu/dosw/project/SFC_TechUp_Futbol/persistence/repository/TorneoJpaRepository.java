package edu.dosw.project.SFC_TechUp_Futbol.persistence.repository;

import edu.dosw.project.SFC_TechUp_Futbol.persistence.entity.TorneoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TorneoJpaRepository extends JpaRepository<TorneoEntity, String> {
}
