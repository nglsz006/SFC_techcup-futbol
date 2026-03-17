package edu.dosw.project.SFC_TechUp_Futbol.core.repository;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Capitan;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class CapitanRepositoryImpl implements CapitanRepository {

    private final Map<Long, Capitan> store = new HashMap<>();
    private long counter = 1;

    @Override
    public Capitan save(Capitan capitan) {
        if (capitan.getId() == null) capitan.setId(counter++);
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
