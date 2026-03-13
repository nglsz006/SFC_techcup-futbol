package edu.dosw.project.SFC_TechUp_Futbol.repository;

import edu.dosw.project.SFC_TechUp_Futbol.model.Torneo;
import java.util.List;
import java.util.Optional;

public interface TorneoRepository {
    Torneo save(Torneo torneo);
    Optional<Torneo> findById(int id);
    List<Torneo> findAll();
}
