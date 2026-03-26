package edu.dosw.project.SFC_TechUp_Futbol.core.repository;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.PerfilDeportivo;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class PerfilDeportivoRepositoryImpl implements PerfilDeportivoRepository {

    private final Map<Long, PerfilDeportivo> store = new HashMap<>();
    private long counter = 1;

    @Override
    public PerfilDeportivo save(PerfilDeportivo perfil) {
        if (perfil.getId() == null) perfil.setId(counter++);
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
