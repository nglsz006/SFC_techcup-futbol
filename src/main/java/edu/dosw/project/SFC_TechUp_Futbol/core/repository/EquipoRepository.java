package edu.dosw.project.SFC_TechUp_Futbol.core.repository;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Equipo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface EquipoRepository extends JpaRepository<Equipo, Integer> {
    Optional<Equipo> findByNombre(String nombre);
    default Optional<Equipo> findById(Long id) { return findById(id.intValue()); }
}
