package edu.dosw.project.SFC_TechUp_Futbol.persistence.repository;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Equipo;
import edu.dosw.project.SFC_TechUp_Futbol.core.repository.EquipoRepository;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.mapper.EquipoMapper;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import edu.dosw.project.SFC_TechUp_Futbol.core.util.IdGeneratorUtil;
import java.util.stream.Collectors;

@Primary
@Repository
public class EquipoRepositoryJpaImpl implements EquipoRepository {

    private final EquipoJpaRepository jpaRepository;
    private final EquipoMapper mapper;

    public EquipoRepositoryJpaImpl(EquipoJpaRepository jpaRepository,
                                   EquipoMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Transactional
    @Override
    public Equipo save(Equipo equipo) {
        if (equipo.getId() == null) equipo.setId(IdGeneratorUtil.generarId());
        return mapper.toDomain(jpaRepository.save(mapper.toEntity(equipo)));
    }

    @Override
    public Optional<Equipo> findById(String id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Equipo> findAll() {
        return jpaRepository.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Equipo> findByCapitanId(String capitanId) {
        return jpaRepository.findByCapitanId(capitanId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
}
