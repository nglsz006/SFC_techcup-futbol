package edu.dosw.project.SFC_TechUp_Futbol;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Torneo;
import edu.dosw.project.SFC_TechUp_Futbol.core.repository.TorneoRepository;

import java.util.*;

public class TorneoRepositoryImpl implements TorneoRepository {
    private final Map<Integer, Torneo> store = new HashMap<>();
    private int nextId = 1;

    @Override
    public Torneo save(Torneo torneo) {
        if (torneo.getId() == 0) {
            torneo.setId(nextId++);
        }
        store.put(torneo.getId(), torneo);
        return torneo;
    }

    @Override
    public Optional<Torneo> findById(int id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<Torneo> findAll() {
        return new ArrayList<>(store.values());
    }
}