package edu.dosw.project.SFC_TechUp_Futbol.persistence.repository;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Alineacion;
import edu.dosw.project.SFC_TechUp_Futbol.core.repository.AlineacionRepository;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.mapper.AlineacionMapper;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import edu.dosw.project.SFC_TechUp_Futbol.core.util.IdGeneratorUtil;
import java.util.stream.Collectors;

@Primary
@Repository
public class AlineacionRepositoryJpaImpl implements AlineacionRepository {

    private final AlineacionJpaRepository jpaRepository;
    private final AlineacionMapper mapper;

    public AlineacionRepositoryJpaImpl(AlineacionJpaRepository jpaRepository,
                                       AlineacionMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Alineacion save(Alineacion alineacion) {
        if (alineacion.getId() == null) alineacion.setId(IdGeneratorUtil.generarId());
        return mapper.toDomain(jpaRepository.save(mapper.toEntity(alineacion)));
    }

    @Override
    public Optional<Alineacion> findById(String id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Alineacion> findAll() {
        return jpaRepository.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
}
