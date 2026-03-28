package edu.dosw.project.SFC_TechUp_Futbol.persistence.repository;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Sancion;
import edu.dosw.project.SFC_TechUp_Futbol.core.repository.SancionRepository;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.mapper.SancionMapper;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Primary
@Repository
public class SancionRepositoryJpaImpl implements SancionRepository {

    private final SancionJpaRepository jpaRepository;
    private final SancionMapper mapper;

    public SancionRepositoryJpaImpl(SancionJpaRepository jpaRepository, SancionMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Sancion save(Sancion sancion) {
        return mapper.toDomain(jpaRepository.save(mapper.toEntity(sancion)));
    }

    @Override
    public Optional<Sancion> findById(Long id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Sancion> findAll() {
        return jpaRepository.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Sancion> findByJugadorId(Long jugadorId) {
        return jpaRepository.findByJugadorId(jugadorId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
}
