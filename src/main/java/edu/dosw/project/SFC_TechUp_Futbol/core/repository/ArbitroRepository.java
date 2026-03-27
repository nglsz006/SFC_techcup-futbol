package edu.dosw.project.SFC_TechUp_Futbol.core.repository;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Arbitro;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ArbitroRepository extends JpaRepository<Arbitro, Long> {
    Optional<Arbitro> findByEmail(String email);
}
