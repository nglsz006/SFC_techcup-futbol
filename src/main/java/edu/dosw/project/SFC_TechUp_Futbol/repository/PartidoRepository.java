package edu.dosw.project.SFC_TechUp_Futbol.repository;

import edu.dosw.project.SFC_TechUp_Futbol.model.Partido;
import edu.dosw.project.SFC_TechUp_Futbol.model.PartidoEstado;

import java.util.List;
import java.util.Optional;

public interface PartidoRepository {
    Partido save(Partido partido);
    Optional<Partido> findById(Long id);
    List<Partido> findByTorneoId(Long torneoId);
    List<Partido> findByEstado(PartidoEstado estado);
    List<Partido> findByEquipoLocalIdOrEquipoVisitanteId(Long equipoLocalId, Long equipoVisitanteId);
}
