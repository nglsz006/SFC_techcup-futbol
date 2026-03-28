package edu.dosw.project.SFC_TechUp_Futbol;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.PerfilDeportivo;
import edu.dosw.project.SFC_TechUp_Futbol.core.repository.PerfilDeportivoRepository;

import java.util.*;

public class PerfilDeportivoRepositoryImpl implements PerfilDeportivoRepository {
    private final Map<Long, PerfilDeportivo> store = new HashMap<>();
    private long nextId = 1L;

    @Override
    public PerfilDeportivo save(PerfilDeportivo perfil) {
        if (perfil.getId() == null) {
            perfil.setId(nextId++);
        }
        store.put(perfil.getId(), perfil);
        return perfil;
    }

    @Override
    public Optional<PerfilDeportivo> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public Optional<PerfilDeportivo> findByJugadorId(Long jugadorId) {
        return store.values().stream()
                .filter(p -> jugadorId.equals(p.getJugadorId()))
                .findFirst();
    }
}
