package edu.dosw.project.SFC_TechUp_Futbol;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Partido;
import edu.dosw.project.SFC_TechUp_Futbol.core.repository.PartidoRepository;

import java.util.*;
import java.util.stream.Collectors;

public class PartidoRepositoryImpl implements PartidoRepository {
    private final Map<Long, Partido> store = new HashMap<>();
    private long nextId = 1L;

    @Override
    public Partido save(Partido partido) {
        if (partido.getId() == null) {
            partido.setId(nextId++);
        }
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
                .filter(p -> p.getTorneo() != null && p.getTorneo().getId() == torneoId.intValue())
                .collect(Collectors.toList());
    }

    @Override
    public List<Partido> findByEstado(Partido.PartidoEstado estado) {
        return store.values().stream()
                .filter(p -> p.getEstado() == estado)
                .collect(Collectors.toList());
    }

    @Override
    public List<Partido> findByEquipoLocalIdOrEquipoVisitanteId(Long equipoLocalId, Long equipoVisitanteId) {
        return store.values().stream()
                .filter(p -> (p.getEquipoLocal() != null && p.getEquipoLocal().getId() == equipoLocalId.intValue())
                        || (p.getEquipoVisitante() != null && p.getEquipoVisitante().getId() == equipoVisitanteId.intValue()))
                .collect(Collectors.toList());
    }
}