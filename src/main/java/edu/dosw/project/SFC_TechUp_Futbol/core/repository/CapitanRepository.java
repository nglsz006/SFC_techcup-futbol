package edu.dosw.project.SFC_TechUp_Futbol.core.repository;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Capitan;

import java.util.List;
import java.util.Optional;

public interface CapitanRepository {
    Capitan save(Capitan capitan);
    Optional<Capitan> findById(String id);
    Optional<Capitan> findByEmail(String email);
    List<Capitan> findAll();
}
