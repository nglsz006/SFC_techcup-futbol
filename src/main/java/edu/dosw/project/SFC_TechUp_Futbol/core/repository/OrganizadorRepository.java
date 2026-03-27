package edu.dosw.project.SFC_TechUp_Futbol.core.repository;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Organizador;

import java.util.List;
import java.util.Optional;

public interface OrganizadorRepository {
    Organizador save(Organizador organizador);
    Optional<Organizador> findById(Long id);
    Optional<Organizador> findByEmail(String email);
    List<Organizador> findAll();
}
