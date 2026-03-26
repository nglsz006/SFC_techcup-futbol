package edu.dosw.project.SFC_TechUp_Futbol.core.repository;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Organizador;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface OrganizadorRepository extends JpaRepository<Organizador, Long> {
    Optional<Organizador> findByEmail(String email);
}