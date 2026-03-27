package edu.dosw.project.SFC_TechUp_Futbol.persistence.repository;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Capitan;
import edu.dosw.project.SFC_TechUp_Futbol.core.repository.CapitanRepository;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.mapper.CapitanMapper;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Primary
@Repository
public class CapitanRepositoryJpaImpl implements CapitanRepository {

    private final CapitanJpaRepository jpaRepository;
    private final CapitanMapper mapper;

    public CapitanRepositoryJpaImpl(CapitanJpaRepository jpaRepository,
                                    CapitanMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Capitan save(Capitan capitan) {
        return mapper.toDomain(jpaRepository.save(mapper.toEntity(capitan)));
    }

    @Override
    public Optional<Capitan> findById(Long id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Capitan> findAll() {
        return jpaRepository.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
}
