package edu.dosw.project.SFC_TechUp_Futbol.persistence.repository;

import edu.dosw.project.SFC_TechUp_Futbol.persistence.entity.JugadorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JugadorJpaRepository extends JpaRepository<JugadorEntity, String> {
    Optional<JugadorEntity> findByEmail(String email);
}
