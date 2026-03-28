package edu.dosw.project.SFC_TechUp_Futbol.persistence.repository;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Jugador;
import edu.dosw.project.SFC_TechUp_Futbol.core.repository.JugadorRepository;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.mapper.JugadorMapper;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Primary
@Repository
public class JugadorRepositoryJpaImpl implements JugadorRepository {

    private final JugadorJpaRepository jpaRepository;
    private final JugadorMapper mapper;

    public JugadorRepositoryJpaImpl(JugadorJpaRepository jpaRepository,
                                    JugadorMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Jugador save(Jugador jugador) {
        return mapper.toDomain(jpaRepository.save(mapper.toEntity(jugador)));
    }

    @Override
    public Optional<Jugador> findById(Long id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Jugador> findAll() {
        return jpaRepository.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
}
