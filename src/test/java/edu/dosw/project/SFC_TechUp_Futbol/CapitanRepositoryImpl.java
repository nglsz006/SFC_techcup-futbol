package edu.dosw.project.SFC_TechUp_Futbol;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Capitan;
import edu.dosw.project.SFC_TechUp_Futbol.core.repository.CapitanRepository;

import java.util.*;

public class CapitanRepositoryImpl implements CapitanRepository {
    private final Map<Long, Capitan> store = new HashMap<>();
    private long nextId = 1L;

    @Override
    public Capitan save(Capitan capitan) {
        if (capitan.getId() == null) {
            capitan.setId(nextId++);
        }
        store.put(capitan.getId(), capitan);
        return capitan;
    }

    @Override
    public Optional<Capitan> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<Capitan> findAll() {
        return new ArrayList<>(store.values());
    }
}