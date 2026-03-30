package edu.dosw.project.SFC_TechUp_Futbol.core.repository;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Administrador;

import java.util.List;
import java.util.Optional;

public interface AdministradorRepository {
    Administrador save(Administrador administrador);
    Optional<Administrador> findById(String id);
    Optional<Administrador> findByEmail(String email);
    List<Administrador> findAll();
}
