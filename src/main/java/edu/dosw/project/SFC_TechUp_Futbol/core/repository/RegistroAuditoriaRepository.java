package edu.dosw.project.SFC_TechUp_Futbol.core.repository;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.RegistroAuditoria;

import java.util.List;

public interface RegistroAuditoriaRepository {
    RegistroAuditoria save(RegistroAuditoria registroAuditoria);
    List<RegistroAuditoria> findAll();
}
