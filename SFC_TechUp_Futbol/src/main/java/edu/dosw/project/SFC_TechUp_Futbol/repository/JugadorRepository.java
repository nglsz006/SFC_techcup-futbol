package edu.dosw.project.SFC_TechUp_Futbol.repository;

import edu.dosw.project.SFC_TechUp_Futbol.model.Jugador;

import java.util.Optional;

public interface JugadorRepository {
    Optional<Jugador> findById(Long id);
}
