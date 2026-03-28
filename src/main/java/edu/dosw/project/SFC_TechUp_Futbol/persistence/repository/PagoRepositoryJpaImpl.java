package edu.dosw.project.SFC_TechUp_Futbol.persistence.repository;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Pago;
import edu.dosw.project.SFC_TechUp_Futbol.core.repository.PagoRepository;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.mapper.PagoMapper;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
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
    public Pago save(Pago pago) {
        return mapper.toDomain(jpaRepository.save(mapper.toEntity(pago)));
    }

    @Override
    public Optional<Pago> findById(Long id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Pago> findByEquipoId(Long equipoId) {
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
    public Optional<Pago> findByEquipoIdAndEstado(Long equipoId, Pago.PagoEstado estado) {
        return jpaRepository.findByEquipoIdAndEstado(equipoId, estado).map(mapper::toDomain);
    }

    @Override
    public boolean existsByEquipoIdAndEstado(Long equipoId, Pago.PagoEstado estado) {
        return jpaRepository.existsByEquipoIdAndEstado(equipoId, estado);
    }
}
