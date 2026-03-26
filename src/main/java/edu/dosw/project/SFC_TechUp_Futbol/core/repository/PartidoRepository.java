package edu.dosw.project.SFC_TechUp_Futbol.core.repository;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Partido;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PartidoRepository extends JpaRepository<Partido, Long> {
    List<Partido> findByTorneoId(Long torneoId);
    List<Partido> findByEstado(Partido.PartidoEstado estado);
    List<Partido> findByEquipoLocalIdOrEquipoVisitanteId(Long localId, Long visitanteId);
}
