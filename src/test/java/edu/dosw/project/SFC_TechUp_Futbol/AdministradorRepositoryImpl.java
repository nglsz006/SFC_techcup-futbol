package edu.dosw.project.SFC_TechUp_Futbol;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Administrador;
import edu.dosw.project.SFC_TechUp_Futbol.core.repository.AdministradorRepository;

import java.util.*;
import java.util.stream.Collectors;

public class AdministradorRepositoryImpl implements AdministradorRepository {
    private final Map<Long, Administrador> store = new HashMap<>();
    private long nextId = 1L;

    @Override
    public Administrador save(Administrador administrador) {
        if (administrador.getId() == null) {
            administrador.setId(nextId++);
        }
        store.put(administrador.getId(), administrador);
        return administrador;
    }

    @Override
    public Optional<Administrador> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public Optional<Administrador> findByEmail(String email) {
        return store.values().stream()
                .filter(a -> a.getEmail().equals(email))
                .findFirst();
    }

    @Override
    public List<Administrador> findAll() {
        return new ArrayList<>(store.values());
    }
}
