package edu.dosw.project.SFC_TechUp_Futbol.core.repository;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Partido;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class PartidoRepositoryImpl implements PartidoRepository {

    private final Map<Long, Partido> store = new HashMap<>();
    private long counter = 1;

    @Override
    public Partido save(Partido partido) {
        if (partido.getId() == null) partido.setId(counter++);
        store.put(partido.getId(), partido);
        return partido;
    }

    @Override
    public Optional<Partido> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<Partido> findByTorneoId(Long torneoId) {
        return store.values().stream()
                .filter(p -> p.getTorneo() != null && torneoId.equals((long) p.getTorneo().getId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Partido> findByEstado(Partido.PartidoEstado estado) {
        return store.values().stream()
                .filter(p -> p.getEstado() == estado)
                .collect(Collectors.toList());
    }

    @Override
    public List<Partido> findByEquipoLocalIdOrEquipoVisitanteId(Long localId, Long visitanteId) {
        return store.values().stream()
                .filter(p -> (p.getEquipoLocal() != null && localId.equals((long) p.getEquipoLocal().getId()))
                        || (p.getEquipoVisitante() != null && visitanteId.equals((long) p.getEquipoVisitante().getId())))
                .collect(Collectors.toList());
    }
}
