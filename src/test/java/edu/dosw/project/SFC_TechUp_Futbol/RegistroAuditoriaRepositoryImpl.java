package edu.dosw.project.SFC_TechUp_Futbol;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.RegistroAuditoria;
import edu.dosw.project.SFC_TechUp_Futbol.core.repository.RegistroAuditoriaRepository;

import java.util.*;

public class RegistroAuditoriaRepositoryImpl implements RegistroAuditoriaRepository {
    private final List<RegistroAuditoria> store = new ArrayList<>();
    private long nextId = 1L;

    @Override
    public RegistroAuditoria save(RegistroAuditoria registro) {
        if (registro.getId() == null) {
            registro.setId(nextId++);
        }
        store.add(registro);
        return registro;
    }

    @Override
    public List<RegistroAuditoria> findAll() {
        return new ArrayList<>(store);
    }
}
