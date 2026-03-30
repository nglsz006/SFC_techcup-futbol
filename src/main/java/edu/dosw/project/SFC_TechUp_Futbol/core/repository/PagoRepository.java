package edu.dosw.project.SFC_TechUp_Futbol.core.repository;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Pago;

import java.util.List;
import java.util.Optional;

public interface PagoRepository {
    Pago save(Pago pago);
    Optional<Pago> findById(String id);
    List<Pago> findByEquipoId(String equipoId);
    List<Pago> findByEstado(Pago.PagoEstado estado);
    Optional<Pago> findByEquipoIdAndEstado(String equipoId, Pago.PagoEstado estado);
    boolean existsByEquipoIdAndEstado(String equipoId, Pago.PagoEstado estado);
}
