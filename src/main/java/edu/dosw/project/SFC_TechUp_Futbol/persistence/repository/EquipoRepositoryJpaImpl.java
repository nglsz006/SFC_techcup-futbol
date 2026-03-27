package edu.dosw.project.SFC_TechUp_Futbol.persistence.repository;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Equipo;
import edu.dosw.project.SFC_TechUp_Futbol.core.repository.EquipoRepository;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.mapper.EquipoMapper;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
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

    @Override
    public Equipo save(Equipo equipo) {
        return mapper.toDomain(jpaRepository.save(mapper.toEntity(equipo)));
    }

    @Override
    public Optional<Equipo> findById(int id) {
        return jpaRepository.findById((long) id).map(mapper::toDomain);
    }

    @Override
    public List<Equipo> findAll() {
        return jpaRepository.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
}
