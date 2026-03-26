package edu.dosw.project.SFC_TechUp_Futbol.core.repository;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.UsuarioRegistrado;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UsuarioRegistradoRepository extends JpaRepository<UsuarioRegistrado, Long> {
    Optional<UsuarioRegistrado> findByEmail(String email);
}
