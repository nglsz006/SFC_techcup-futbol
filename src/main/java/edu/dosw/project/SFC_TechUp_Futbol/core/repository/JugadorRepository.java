package edu.dosw.project.SFC_TechUp_Futbol.core.repository;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Jugador;

import java.util.List;
import java.util.Optional;

public interface JugadorRepository {
    Jugador save(Jugador jugador);
    Optional<Jugador> findById(String id);
    List<Jugador> findAll();
}
