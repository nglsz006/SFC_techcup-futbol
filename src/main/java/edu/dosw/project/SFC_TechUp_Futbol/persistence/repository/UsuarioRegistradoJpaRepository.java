package edu.dosw.project.SFC_TechUp_Futbol.persistence.repository;

import edu.dosw.project.SFC_TechUp_Futbol.persistence.entity.UsuarioRegistradoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRegistradoJpaRepository extends JpaRepository<UsuarioRegistradoEntity, String> {
    Optional<UsuarioRegistradoEntity> findByEmail(String email);
}
