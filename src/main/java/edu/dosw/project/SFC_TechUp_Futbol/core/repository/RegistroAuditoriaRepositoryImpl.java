package edu.dosw.project.SFC_TechUp_Futbol.core.repository;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.RegistroAuditoria;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class RegistroAuditoriaRepositoryImpl implements RegistroAuditoriaRepository {

    private static final List<RegistroAuditoria> STORE = Collections.synchronizedList(new ArrayList<>());
    private static final AtomicLong COUNTER = new AtomicLong(1);

    @Override
    public RegistroAuditoria save(RegistroAuditoria registroAuditoria) {
        if (registroAuditoria.getId() == null) {
            registroAuditoria.setId(COUNTER.getAndIncrement());
        }
        STORE.add(registroAuditoria);
        return registroAuditoria;
    }

    @Override
    public List<RegistroAuditoria> findAll() {
        synchronized (STORE) {
            return new ArrayList<>(STORE);
        }
    }

    public static void resetStore() {
        synchronized (STORE) {
            STORE.clear();
            COUNTER.set(1);
        }
    }
}
