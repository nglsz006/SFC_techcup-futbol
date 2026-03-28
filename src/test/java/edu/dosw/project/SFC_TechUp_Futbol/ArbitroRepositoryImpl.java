package edu.dosw.project.SFC_TechUp_Futbol;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Arbitro;
import edu.dosw.project.SFC_TechUp_Futbol.core.repository.ArbitroRepository;

import java.util.*;
import java.util.stream.Collectors;

public class ArbitroRepositoryImpl implements ArbitroRepository {
    private final Map<Long, Arbitro> store = new HashMap<>();
    private long nextId = 1L;

    @Override
    public Arbitro save(Arbitro arbitro) {
        if (arbitro.getId() == null) {
            arbitro.setId(nextId++);
        }
        store.put(arbitro.getId(), arbitro);
        return arbitro;
    }

    @Override
    public Optional<Arbitro> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public Optional<Arbitro> findByEmail(String email) {
        return store.values().stream()
                .filter(a -> a.getEmail().equals(email))
                .findFirst();
    }

    @Override
    public List<Arbitro> findAll() {
        return new ArrayList<>(store.values());
    }
}