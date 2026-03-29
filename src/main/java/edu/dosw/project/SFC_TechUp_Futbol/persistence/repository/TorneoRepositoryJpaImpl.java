package edu.dosw.project.SFC_TechUp_Futbol.persistence.repository;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Torneo;
import edu.dosw.project.SFC_TechUp_Futbol.core.repository.TorneoRepository;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.mapper.TorneoMapper;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import edu.dosw.project.SFC_TechUp_Futbol.core.util.IdGeneratorUtil;
import java.util.stream.Collectors;

@Primary
@Repository
public class TorneoRepositoryJpaImpl implements TorneoRepository {

    private final TorneoJpaRepository jpaRepository;
    private final TorneoMapper mapper;

    public TorneoRepositoryJpaImpl(TorneoJpaRepository jpaRepository,
                                   TorneoMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Torneo save(Torneo torneo) {
        if (torneo.getId() == null) torneo.setId(IdGeneratorUtil.generarId());
        return mapper.toDomain(jpaRepository.save(mapper.toEntity(torneo)));
    }

    @Override
    public Optional<Torneo> findById(String id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Torneo> findAll() {
        return jpaRepository.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
}
