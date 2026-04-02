package edu.dosw.project.SFC_TechUp_Futbol.persistence.repository;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Pago;
import edu.dosw.project.SFC_TechUp_Futbol.core.repository.PagoRepository;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.mapper.PagoMapper;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import edu.dosw.project.SFC_TechUp_Futbol.core.util.IdGeneratorUtil;
import java.util.stream.Collectors;

@Primary
@Repository
public class PagoRepositoryJpaImpl implements PagoRepository {

    private final PagoJpaRepository jpaRepository;
    private final PagoMapper mapper;

    public PagoRepositoryJpaImpl(PagoJpaRepository jpaRepository,
                                 PagoMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public Pago save(Pago pago) {
        if (pago.getId() == null) pago.setId(IdGeneratorUtil.generarId());
        return mapper.toDomain(jpaRepository.save(mapper.toEntity(pago)));
    }

    @Override
    public Optional<Pago> findById(String id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Pago> findByEquipoId(String equipoId) {
        return jpaRepository.findByEquipoId(equipoId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Pago> findByEstado(Pago.PagoEstado estado) {
        return jpaRepository.findByEstado(estado).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Pago> findByEquipoIdAndEstado(String equipoId, Pago.PagoEstado estado) {
        return jpaRepository.findByEquipoIdAndEstado(equipoId, estado).map(mapper::toDomain);
    }

    @Override
    public boolean existsByEquipoIdAndEstado(String equipoId, Pago.PagoEstado estado) {
        return jpaRepository.existsByEquipoIdAndEstado(equipoId, estado);
    }
}
