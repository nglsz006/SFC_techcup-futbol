package edu.dosw.project.SFC_TechUp_Futbol.persistence.repository;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Torneo;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.entity.TorneoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TorneoJpaRepository extends JpaRepository<TorneoEntity, String> {
    List<TorneoEntity> findByEstado(Torneo.EstadoTorneo estado);
}
