package edu.dosw.project.SFC_TechUp_Futbol.persistence.repository;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.PerfilDeportivo;
import edu.dosw.project.SFC_TechUp_Futbol.core.repository.PerfilDeportivoRepository;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.mapper.PerfilDeportivoMapper;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Primary
@Repository
public class PerfilDeportivoRepositoryJpaImpl implements PerfilDeportivoRepository {

    private final PerfilDeportivoJpaRepository jpaRepository;
    private final PerfilDeportivoMapper mapper;

    public PerfilDeportivoRepositoryJpaImpl(PerfilDeportivoJpaRepository jpaRepository,
                                            PerfilDeportivoMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public PerfilDeportivo save(PerfilDeportivo perfil) {
        return mapper.toDomain(jpaRepository.save(mapper.toEntity(perfil)));
    }

    @Override
    public Optional<PerfilDeportivo> findById(Long id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<PerfilDeportivo> findByJugadorId(Long jugadorId) {
        return jpaRepository.findByJugadorId(jugadorId).map(mapper::toDomain);
    }
}
