package edu.dosw.project.SFC_TechUp_Futbol.core.repository;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Arbitro;

import java.util.List;
import java.util.Optional;

public interface ArbitroRepository {
    Arbitro save(Arbitro arbitro);
    Optional<Arbitro> findById(String id);
    Optional<Arbitro> findByEmail(String email);
    List<Arbitro> findAll();
}
