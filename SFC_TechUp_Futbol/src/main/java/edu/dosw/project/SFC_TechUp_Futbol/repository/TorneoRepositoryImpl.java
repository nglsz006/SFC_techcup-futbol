package edu.dosw.project.SFC_TechUp_Futbol.repository;

import edu.dosw.project.SFC_TechUp_Futbol.model.Torneo;
import java.util.*;

public class TorneoRepositoryImpl implements TorneoRepository {
    private static Map<Integer, Torneo> storage = new HashMap<>();
    private static int contadorId = 0;

    @Override
    public Torneo save(Torneo torneo) {
        if (torneo.getId() == 0) {
            contadorId++;
            torneo.setId(contadorId);
        }
        storage.put(torneo.getId(), torneo);
        return torneo;
    }

    @Override
    public Optional<Torneo> findById(int id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<Torneo> findAll() {
        return new ArrayList<>(storage.values());
    }
}
