package edu.dosw.project.SFC_TechUp_Futbol.core.repository;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Partido;

import java.util.List;
import java.util.Optional;

public interface PartidoRepository {
    Partido save(Partido partido);
    Optional<Partido> findById(String id);
    List<Partido> findByTorneoId(String torneoId);
    List<Partido> findByEstado(Partido.PartidoEstado estado);
    List<Partido> findByEquipoLocalIdOrEquipoVisitanteId(String equipoLocalId, String equipoVisitanteId);
}
