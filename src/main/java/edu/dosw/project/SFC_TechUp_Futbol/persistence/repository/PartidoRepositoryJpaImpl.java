package edu.dosw.project.SFC_TechUp_Futbol.persistence.repository;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Partido;
import edu.dosw.project.SFC_TechUp_Futbol.core.repository.PartidoRepository;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.mapper.PartidoMapper;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import edu.dosw.project.SFC_TechUp_Futbol.core.util.IdGeneratorUtil;
import java.util.stream.Collectors;

@Primary
@Repository
public class PartidoRepositoryJpaImpl implements PartidoRepository {

    private final PartidoJpaRepository jpaRepository;
    private final PartidoMapper mapper;

    public PartidoRepositoryJpaImpl(PartidoJpaRepository jpaRepository,
                                    PartidoMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Partido save(Partido partido) {
        if (partido.getId() == null) partido.setId(IdGeneratorUtil.generarId());
        return mapper.toDomain(jpaRepository.save(mapper.toEntity(partido)));
    }

    @Override
    public Optional<Partido> findById(String id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Partido> findByTorneoId(String torneoId) {
        return jpaRepository.findByTorneoId(torneoId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Partido> findByEstado(Partido.PartidoEstado estado) {
        return jpaRepository.findByEstado(estado).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Partido> findByEquipoLocalIdOrEquipoVisitanteId(String equipoLocalId, String equipoVisitanteId) {
        return jpaRepository.findByEquipoLocalIdOrEquipoVisitanteId(equipoLocalId, equipoVisitanteId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
}
