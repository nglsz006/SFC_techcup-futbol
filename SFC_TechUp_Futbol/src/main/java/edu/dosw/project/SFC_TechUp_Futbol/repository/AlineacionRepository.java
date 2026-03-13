package edu.dosw.project.SFC_TechUp_Futbol.repository;

import edu.dosw.project.SFC_TechUp_Futbol.model.Alineacion;
import java.util.List;
import java.util.Optional;

public interface AlineacionRepository {
    Alineacion save(Alineacion alineacion);
    Optional<Alineacion> findById(int id);
    List<Alineacion> findAll();
}
