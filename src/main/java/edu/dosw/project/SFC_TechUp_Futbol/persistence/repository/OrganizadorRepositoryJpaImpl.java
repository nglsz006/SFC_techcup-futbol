package edu.dosw.project.SFC_TechUp_Futbol.persistence.repository;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Organizador;
import edu.dosw.project.SFC_TechUp_Futbol.core.repository.OrganizadorRepository;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.mapper.OrganizadorMapper;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import edu.dosw.project.SFC_TechUp_Futbol.core.util.IdGeneratorUtil;
import java.util.stream.Collectors;

@Primary
@Repository
public class OrganizadorRepositoryJpaImpl implements OrganizadorRepository {

    private final OrganizadorJpaRepository jpaRepository;
    private final OrganizadorMapper mapper;

    public OrganizadorRepositoryJpaImpl(OrganizadorJpaRepository jpaRepository, OrganizadorMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public Organizador save(Organizador organizador) {
        if (organizador.getId() == null) organizador.setId(IdGeneratorUtil.generarId());
        return mapper.toDomain(jpaRepository.save(mapper.toEntity(organizador)));
    }

    @Override
    public Optional<Organizador> findById(String id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<Organizador> findByEmail(String email) {
        return jpaRepository.findByEmail(email).map(mapper::toDomain);
    }

    @Override
    public List<Organizador> findAll() {
        return jpaRepository.findAll().stream().map(mapper::toDomain).collect(Collectors.toList());
    }
}
