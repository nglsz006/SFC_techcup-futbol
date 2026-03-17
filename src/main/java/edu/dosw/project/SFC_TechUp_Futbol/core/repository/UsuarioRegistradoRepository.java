package edu.dosw.project.SFC_TechUp_Futbol.core.repository;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.UsuarioRegistrado;

import java.util.List;
import java.util.Optional;

public interface UsuarioRegistradoRepository {
    UsuarioRegistrado save(UsuarioRegistrado usuario);
    Optional<UsuarioRegistrado> findByEmail(String email);
    List<UsuarioRegistrado> findAll();
}
