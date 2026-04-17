package edu.dosw.project.SFC_TechUp_Futbol.persistence.repository;

import edu.dosw.project.SFC_TechUp_Futbol.persistence.entity.InvitacionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InvitacionJpaRepository extends JpaRepository<InvitacionEntity, String> {
    List<InvitacionEntity> findByJugadorId(String jugadorId);
    List<InvitacionEntity> findByEquipoId(String equipoId);
}
