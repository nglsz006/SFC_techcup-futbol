package edu.dosw.project.SFC_TechUp_Futbol.persistence.repository;

import edu.dosw.project.SFC_TechUp_Futbol.persistence.entity.CapitanEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CapitanJpaRepository extends JpaRepository<CapitanEntity, String> {
    Optional<CapitanEntity> findByEmail(String email);
}
