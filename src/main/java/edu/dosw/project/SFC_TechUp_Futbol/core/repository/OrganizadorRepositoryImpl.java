package edu.dosw.project.SFC_TechUp_Futbol.core.repository;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Organizador;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class OrganizadorRepositoryImpl implements OrganizadorRepository {

    private final Map<Long, Organizador> store = new HashMap<>();
    private long counter = 1;

    @Override
    public Organizador save(Organizador organizador) {
        if (organizador.getId() == null) organizador.setId(counter++);
        store.put(organizador.getId(), organizador);
        return organizador;
    }

    @Override
    public Optional<Organizador> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<Organizador> findAll() {
        return new ArrayList<>(store.values());
    }
}
