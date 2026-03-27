package edu.dosw.project.SFC_TechUp_Futbol.persistence.repository;

import edu.dosw.project.SFC_TechUp_Futbol.persistence.entity.JugadorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JugadorJpaRepository extends JpaRepository<JugadorEntity, Long> {
}
