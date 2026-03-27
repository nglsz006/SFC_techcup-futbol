package edu.dosw.project.SFC_TechUp_Futbol.persistence.repository;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Partido;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.entity.PartidoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PartidoJpaRepository extends JpaRepository<PartidoEntity, Long> {
    List<PartidoEntity> findByTorneoId(Long torneoId);
    List<PartidoEntity> findByEstado(Partido.PartidoEstado estado);
    List<PartidoEntity> findByEquipoLocalIdOrEquipoVisitanteId(Long equipoLocalId, Long equipoVisitanteId);
}
