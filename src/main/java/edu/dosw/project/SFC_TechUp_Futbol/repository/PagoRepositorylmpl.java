package edu.dosw.project.SFC_TechUp_Futbol.core.repository;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Pago;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class PagoRepositoryImpl implements PagoRepository {

    private final Map<Long, Pago> store = new HashMap<>();
    private long counter = 1;

    @Override
    public Pago save(Pago pago) {
        if (pago.getId() == null) pago.setId(counter++);
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
                .filter(p -> p.getEquipo() != null && equipoId.equals((long) p.getEquipo().getId()))
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
                .filter(p -> p.getEquipo() != null && equipoId.equals((long) p.getEquipo().getId()) && p.getEstado() == estado)
                .findFirst();
    }

    @Override
    public boolean existsByEquipoIdAndEstado(Long equipoId, Pago.PagoEstado estado) {
        return findByEquipoIdAndEstado(equipoId, estado).isPresent();
    }
}