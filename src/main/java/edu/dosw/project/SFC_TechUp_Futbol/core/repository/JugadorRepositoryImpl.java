package edu.dosw.project.SFC_TechUp_Futbol.core.repository;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Jugador;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class JugadorRepositoryImpl implements JugadorRepository {

    private final Map<Long, Jugador> store = new HashMap<>();

    public Jugador save(Jugador jugador) {
        store.put(jugador.getId(), jugador);
        return jugador;
    }

    @Override
    public Optional<Jugador> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    public List<Jugador> findAll() {
        return new ArrayList<>(store.values());
    }
}
