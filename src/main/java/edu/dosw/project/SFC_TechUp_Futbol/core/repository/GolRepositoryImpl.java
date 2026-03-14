package edu.dosw.project.SFC_TechUp_Futbol.core.repository;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Gol;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class GolRepositoryImpl implements GolRepository {

    private final List<Gol> store = new ArrayList<>();

    @Override
    public Gol save(Gol gol) {
        store.add(gol);
        return gol;
    }

    @Override
    public List<Gol> findByPartidoId(Long partidoId) {
        return store.stream()
                .filter(g -> g.getPartido() != null && partidoId.equals(g.getPartido().getId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Gol> findByJugadorId(Long jugadorId) {
        return store.stream()
                .filter(g -> g.getJugador() != null && jugadorId.equals(g.getJugador().getId()))
                .collect(Collectors.toList());
    }
}
