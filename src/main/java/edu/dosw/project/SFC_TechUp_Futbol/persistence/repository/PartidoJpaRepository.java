package edu.dosw.project.SFC_TechUp_Futbol.persistence.repository;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Partido;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.entity.PartidoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PartidoJpaRepository extends JpaRepository<PartidoEntity, String> {
    List<PartidoEntity> findByTorneoId(String torneoId);
    List<PartidoEntity> findByEstado(Partido.PartidoEstado estado);
    List<PartidoEntity> findByEquipoLocalIdOrEquipoVisitanteId(String equipoLocalId, String equipoVisitanteId);
    List<PartidoEntity> findByTorneoIdAndFase(String torneoId, Partido.Fase fase);
}
