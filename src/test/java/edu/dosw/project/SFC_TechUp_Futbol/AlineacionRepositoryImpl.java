package edu.dosw.project.SFC_TechUp_Futbol;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Alineacion;
import edu.dosw.project.SFC_TechUp_Futbol.core.repository.AlineacionRepository;

import java.util.*;

public class AlineacionRepositoryImpl implements AlineacionRepository {
    private final Map<Integer, Alineacion> store = new HashMap<>();
    private int nextId = 1;

    @Override
    public Alineacion save(Alineacion alineacion) {
        if (alineacion.getId() == 0) {
            alineacion.setId(nextId++);
        }
        store.put(alineacion.getId(), alineacion);
        return alineacion;
    }

    @Override
    public Optional<Alineacion> findById(int id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<Alineacion> findAll() {
        return new ArrayList<>(store.values());
    }
}