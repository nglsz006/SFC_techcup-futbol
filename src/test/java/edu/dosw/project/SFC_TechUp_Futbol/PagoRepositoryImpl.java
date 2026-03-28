package edu.dosw.project.SFC_TechUp_Futbol;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Pago;
import edu.dosw.project.SFC_TechUp_Futbol.core.repository.PagoRepository;

import java.util.*;
import java.util.stream.Collectors;

public class PagoRepositoryImpl implements PagoRepository {
    private final Map<Long, Pago> store = new HashMap<>();
    private long nextId = 1L;

    @Override
    public Pago save(Pago pago) {
        if (pago.getId() == null) {
            pago.setId(nextId++);
        }
        store.put(pago.getId(), pago);
        return pago;
    }

    @Override
    public Optional<Pago> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<Pago> findByEquipoId(Long equipoId) {
        return store.values().stream()
                .filter(p -> p.getEquipo() != null && p.getEquipo().getId() == equipoId.intValue())
                .collect(Collectors.toList());
    }

    @Override
    public List<Pago> findByEstado(Pago.PagoEstado estado) {
        return store.values().stream()
                .filter(p -> p.getEstado() == estado)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Pago> findByEquipoIdAndEstado(Long equipoId, Pago.PagoEstado estado) {
        return store.values().stream()
                .filter(p -> p.getEquipo() != null && p.getEquipo().getId() == equipoId.intValue()
                        && p.getEstado() == estado)
                .findFirst();
    }

    @Override
    public boolean existsByEquipoIdAndEstado(Long equipoId, Pago.PagoEstado estado) {
        return store.values().stream()
                .anyMatch(p -> p.getEquipo() != null && p.getEquipo().getId() == equipoId.intValue()
                        && p.getEstado() == estado);
    }
}