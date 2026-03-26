package edu.dosw.project.SFC_TechUp_Futbol.core.repository;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface PagoRepository extends JpaRepository<Pago, Long> {
    List<Pago> findByEquipoId(Long equipoId);
    List<Pago> findByEstado(Pago.PagoEstado estado);
    Optional<Pago> findByEquipoIdAndEstado(Long equipoId, Pago.PagoEstado estado);
    boolean existsByEquipoIdAndEstado(Long equipoId, Pago.PagoEstado estado);
}
