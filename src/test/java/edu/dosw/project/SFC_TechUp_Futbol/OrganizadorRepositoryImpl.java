package edu.dosw.project.SFC_TechUp_Futbol;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Organizador;
import edu.dosw.project.SFC_TechUp_Futbol.core.repository.OrganizadorRepository;

import java.util.*;
import java.util.stream.Collectors;

public class OrganizadorRepositoryImpl implements OrganizadorRepository {
    private final Map<Long, Organizador> store = new HashMap<>();
    private long nextId = 1L;

    @Override
    public Organizador save(Organizador organizador) {
        if (organizador.getId() == null) {
            organizador.setId(nextId++);
        }
        store.put(organizador.getId(), organizador);
        return organizador;
    }

    @Override
    public Optional<Organizador> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public Optional<Organizador> findByEmail(String email) {
        return store.values().stream()
                .filter(o -> o.getEmail().equals(email))
                .findFirst();
    }

    @Override
    public List<Organizador> findAll() {
        return new ArrayList<>(store.values());
    }
}