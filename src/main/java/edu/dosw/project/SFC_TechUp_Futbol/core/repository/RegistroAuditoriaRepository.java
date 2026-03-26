package edu.dosw.project.SFC_TechUp_Futbol.core.repository;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.RegistroAuditoria;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.TipoAccionAuditoria;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RegistroAuditoriaRepository extends JpaRepository<RegistroAuditoria, Long> {
    List<RegistroAuditoria> findByAdministradorId(Long administradorId);
    List<RegistroAuditoria> findByTipoAccion(TipoAccionAuditoria tipoAccion);
}
