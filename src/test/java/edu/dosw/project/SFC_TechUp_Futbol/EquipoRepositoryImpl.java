package edu.dosw.project.SFC_TechUp_Futbol;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Equipo;
import edu.dosw.project.SFC_TechUp_Futbol.core.repository.EquipoRepository;

import java.util.*;

public class EquipoRepositoryImpl implements EquipoRepository {
    private final Map<Integer, Equipo> store = new HashMap<>();
    private int nextId = 1;

    @Override
    public Equipo save(Equipo equipo) {
        if (equipo.getId() == 0) {
            equipo.setId(nextId++);
        }
        store.put(equipo.getId(), equipo);
        return equipo;
    }

    @Override
    public Optional<Equipo> findById(int id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<Equipo> findAll() {
        return new ArrayList<>(store.values());
    }
}