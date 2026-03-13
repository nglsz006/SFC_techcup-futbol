package edu.dosw.project.SFC_TechUp_Futbol.repository;

import edu.dosw.project.SFC_TechUp_Futbol.model.Equipo;
import java.util.*;

public class EquipoRepositoryImpl implements EquipoRepository {
    private static Map<Integer, Equipo> storage = new HashMap<>();
    private static int contadorId = 0;

    @Override
    public Equipo save(Equipo equipo) {
        if (equipo.getId() == 0) {
            contadorId++;
            equipo.setId(contadorId);
        }
        storage.put(equipo.getId(), equipo);
        return equipo;
    }

    @Override
    public Optional<Equipo> findById(int id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<Equipo> findAll() {
        return new ArrayList<>(storage.values());
    }
}
