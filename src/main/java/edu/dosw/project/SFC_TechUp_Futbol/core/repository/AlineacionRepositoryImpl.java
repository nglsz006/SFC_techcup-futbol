package edu.dosw.project.SFC_TechUp_Futbol.core.repository;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Alineacion;
import java.util.*;
import org.springframework.stereotype.Repository;

@Repository
public class AlineacionRepositoryImpl implements AlineacionRepository {
    private static Map<Integer, Alineacion> storage = new HashMap<>();
    private static int contadorId = 0;

    @Override
    public Alineacion save(Alineacion alineacion) {
        if (alineacion.getId() == 0) {
            contadorId++;
            alineacion.setId(contadorId);
        }
        storage.put(alineacion.getId(), alineacion);
        return alineacion;
    }

    @Override
    public Optional<Alineacion> findById(int id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<Alineacion> findAll() {
        return new ArrayList<>(storage.values());
    }
}

