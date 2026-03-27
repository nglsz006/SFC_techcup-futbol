package edu.dosw.project.SFC_TechUp_Futbol.persistence.repository;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Pago;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.entity.PagoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PagoJpaRepository extends JpaRepository<PagoEntity, Long> {
    List<PagoEntity> findByEquipoId(Long equipoId);
    List<PagoEntity> findByEstado(Pago.PagoEstado estado);
    Optional<PagoEntity> findByEquipoIdAndEstado(Long equipoId, Pago.PagoEstado estado);
    boolean existsByEquipoIdAndEstado(Long equipoId, Pago.PagoEstado estado);
}
