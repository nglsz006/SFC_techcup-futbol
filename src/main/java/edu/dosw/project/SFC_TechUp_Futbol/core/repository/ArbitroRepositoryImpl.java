package edu.dosw.project.SFC_TechUp_Futbol.core.repository;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Arbitro;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class ArbitroRepositoryImpl implements ArbitroRepository {

    private final Map<Long, Arbitro> store = new HashMap<>();
    private long counter = 1;

    @Override
    public Arbitro save(Arbitro arbitro) {
        if (arbitro.getId() == null) arbitro.setId(counter++);
        store.put(arbitro.getId(), arbitro);
        return arbitro;
    }

    @Override
    public Optional<Arbitro> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<Arbitro> findAll() {
        return new ArrayList<>(store.values());
    }
}
