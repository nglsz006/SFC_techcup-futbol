package edu.dosw.project.SFC_TechUp_Futbol.repository;

import edu.dosw.project.SFC_TechUp_Futbol.model.Pago;
import edu.dosw.project.SFC_TechUp_Futbol.model.PagoEstado;

import java.util.List;
import java.util.Optional;

public interface PagoRepository {
    Pago save(Pago pago);
    Optional<Pago> findById(Long id);
    List<Pago> findByEquipoId(Long equipoId);
    List<Pago> findByEstado(PagoEstado estado);
    Optional<Pago> findByEquipoIdAndEstado(Long equipoId, PagoEstado estado);
    boolean existsByEquipoIdAndEstado(Long equipoId, PagoEstado estado);
}
