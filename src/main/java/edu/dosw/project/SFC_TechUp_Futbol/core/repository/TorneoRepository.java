package edu.dosw.project.SFC_TechUp_Futbol.core.repository;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Torneo;
import java.util.List;
import java.util.Optional;

public interface TorneoRepository {
    Torneo save(Torneo torneo);
    Optional<Torneo> findById(String id);
    List<Torneo> findAll();
    List<Torneo> findByEstado(Torneo.EstadoTorneo estado);
}

