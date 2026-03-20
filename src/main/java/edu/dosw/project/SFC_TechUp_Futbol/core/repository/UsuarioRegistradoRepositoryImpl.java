package edu.dosw.project.SFC_TechUp_Futbol.core.repository;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.UsuarioRegistrado;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class UsuarioRegistradoRepositoryImpl implements UsuarioRegistradoRepository {

    private final Map<Long, UsuarioRegistrado> store = new HashMap<>();
    private long counter = 1;

    @Override
    public UsuarioRegistrado save(UsuarioRegistrado usuario) {
        if (usuario.getId() == null) usuario.setId(counter++);
        store.put(usuario.getId(), usuario);
        return usuario;
    }

    @Override
    public Optional<UsuarioRegistrado> findByEmail(String email) {
        return store.values().stream()
                .filter(u -> u.getEmail().equalsIgnoreCase(email))
                .findFirst();
    }

    @Override
    public List<UsuarioRegistrado> findAll() {
        return new ArrayList<>(store.values());
    }
}
