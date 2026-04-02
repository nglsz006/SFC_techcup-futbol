package edu.dosw.project.SFC_TechUp_Futbol.persistence.repository;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Arbitro;
import edu.dosw.project.SFC_TechUp_Futbol.core.repository.ArbitroRepository;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.mapper.ArbitroMapper;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import edu.dosw.project.SFC_TechUp_Futbol.core.util.IdGeneratorUtil;
import java.util.stream.Collectors;

@Primary
@Repository
public class ArbitroRepositoryJpaImpl implements ArbitroRepository {

    private final ArbitroJpaRepository jpaRepository;
    private final ArbitroMapper mapper;

    public ArbitroRepositoryJpaImpl(ArbitroJpaRepository jpaRepository, ArbitroMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public Arbitro save(Arbitro arbitro) {
        if (arbitro.getId() == null) arbitro.setId(IdGeneratorUtil.generarId());
        return mapper.toDomain(jpaRepository.save(mapper.toEntity(arbitro)));
    }

    @Override
    public Optional<Arbitro> findById(String id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<Arbitro> findByEmail(String email) {
        return jpaRepository.findByEmail(email).map(mapper::toDomain);
    }

    @Override
    public List<Arbitro> findAll() {
        return jpaRepository.findAll().stream().map(mapper::toDomain).collect(Collectors.toList());
    }
}
