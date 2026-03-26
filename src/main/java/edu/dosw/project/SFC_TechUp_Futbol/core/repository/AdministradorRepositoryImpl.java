package edu.dosw.project.SFC_TechUp_Futbol.core.repository;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Administrador;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class AdministradorRepositoryImpl implements AdministradorRepository {

    private final Map<Long, Administrador> store = new HashMap<>();
    private long counter = 1;

    @Override
    public Administrador save(Administrador administrador) {
        if (administrador.getId() == null) {
            administrador.setId(counter++);
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
                .filter(administrador -> administrador.getEmail().equalsIgnoreCase(email))
                .findFirst();
    }

    @Override
    public List<Administrador> findAll() {
        return new ArrayList<>(store.values());
    }
}
