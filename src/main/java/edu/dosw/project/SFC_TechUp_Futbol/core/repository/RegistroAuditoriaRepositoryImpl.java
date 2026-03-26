package edu.dosw.project.SFC_TechUp_Futbol.core.repository;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.RegistroAuditoria;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class RegistroAuditoriaRepositoryImpl implements RegistroAuditoriaRepository {

    private final List<RegistroAuditoria> store = new ArrayList<>();
    private long counter = 1;

    @Override
    public RegistroAuditoria save(RegistroAuditoria registroAuditoria) {
        if (registroAuditoria.getId() == null) {
            registroAuditoria.setId(counter++);
        }
        store.add(registroAuditoria);
        return registroAuditoria;
    }

    @Override
    public List<RegistroAuditoria> findAll() {
        return new ArrayList<>(store);
    }
}
