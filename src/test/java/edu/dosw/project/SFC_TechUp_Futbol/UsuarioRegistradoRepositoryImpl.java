package edu.dosw.project.SFC_TechUp_Futbol;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.UsuarioRegistrado;
import edu.dosw.project.SFC_TechUp_Futbol.core.repository.UsuarioRegistradoRepository;

import java.util.*;
import java.util.stream.Collectors;

public class UsuarioRegistradoRepositoryImpl implements UsuarioRegistradoRepository {
    private final Map<Long, UsuarioRegistrado> store = new HashMap<>();
    private long nextId = 1L;

    @Override
    public UsuarioRegistrado save(UsuarioRegistrado usuario) {
        if (usuario.getId() == null) {
            usuario.setId(nextId++);
        }
        store.put(usuario.getId(), usuario);
        return usuario;
    }

    @Override
    public Optional<UsuarioRegistrado> findByEmail(String email) {
        return store.values().stream()
                .filter(u -> u.getEmail().equals(email))
                .findFirst();
    }

    @Override
    public List<UsuarioRegistrado> findAll() {
        return new ArrayList<>(store.values());
    }
}
