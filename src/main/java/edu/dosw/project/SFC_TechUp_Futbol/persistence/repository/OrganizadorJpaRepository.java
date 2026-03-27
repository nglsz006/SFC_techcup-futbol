package edu.dosw.project.SFC_TechUp_Futbol.persistence.repository;

import edu.dosw.project.SFC_TechUp_Futbol.persistence.entity.OrganizadorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrganizadorJpaRepository extends JpaRepository<OrganizadorEntity, Long> {
    Optional<OrganizadorEntity> findByEmail(String email);
}
