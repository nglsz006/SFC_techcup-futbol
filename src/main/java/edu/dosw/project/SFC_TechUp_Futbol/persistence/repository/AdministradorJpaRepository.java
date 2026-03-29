package edu.dosw.project.SFC_TechUp_Futbol.persistence.repository;

import edu.dosw.project.SFC_TechUp_Futbol.persistence.entity.AdministradorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdministradorJpaRepository extends JpaRepository<AdministradorEntity, String> {
    Optional<AdministradorEntity> findByEmail(String email);
}
