package edu.dosw.project.SFC_TechUp_Futbol.core.repository;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Jugador;

import java.util.Optional;

public interface JugadorRepository {
    Optional<Jugador> findById(Long id);
}

