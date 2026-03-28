package edu.dosw.project.SFC_TechUp_Futbol;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Jugador;
import edu.dosw.project.SFC_TechUp_Futbol.core.repository.JugadorRepository;

import java.util.*;

public class JugadorRepositoryImpl implements JugadorRepository {
    private final Map<Long, Jugador> store = new HashMap<>();
    private long nextId = 1L;

    @Override
    public Jugador save(Jugador jugador) {
        if (jugador.getId() == null) {
            jugador.setId(nextId++);
        }
        store.put(jugador.getId(), jugador);
        return jugador;
    }

    @Override
    public Optional<Jugador> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<Jugador> findAll() {
        return new ArrayList<>(store.values());
    }
}