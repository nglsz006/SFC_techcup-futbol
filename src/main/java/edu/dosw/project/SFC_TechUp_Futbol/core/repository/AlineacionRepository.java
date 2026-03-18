package edu.dosw.project.SFC_TechUp_Futbol.core.repository;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Alineacion;

import java.util.List;
import java.util.Optional;

public interface AlineacionRepository {
    Alineacion save(Alineacion alineacion);
    Optional<Alineacion> findById(int id);
    List<Alineacion> findAll();
}
