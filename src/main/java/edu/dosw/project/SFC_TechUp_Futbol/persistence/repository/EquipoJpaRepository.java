package edu.dosw.project.SFC_TechUp_Futbol.persistence.repository;

import edu.dosw.project.SFC_TechUp_Futbol.persistence.entity.EquipoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EquipoJpaRepository extends JpaRepository<EquipoEntity, String> {
    List<EquipoEntity> findByCapitanId(String capitanId);
    boolean existsByNombre(String nombre);
}
