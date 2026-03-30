package edu.dosw.project.SFC_TechUp_Futbol.persistence.repository;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.RegistroAuditoria;
import edu.dosw.project.SFC_TechUp_Futbol.core.repository.RegistroAuditoriaRepository;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.mapper.RegistroAuditoriaMapper;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.List;
import edu.dosw.project.SFC_TechUp_Futbol.core.util.IdGeneratorUtil;
import java.util.stream.Collectors;

@Primary
@Repository
public class RegistroAuditoriaRepositoryJpaImpl implements RegistroAuditoriaRepository {

    private final RegistroAuditoriaJpaRepository jpaRepository;
    private final RegistroAuditoriaMapper mapper;

    public RegistroAuditoriaRepositoryJpaImpl(RegistroAuditoriaJpaRepository jpaRepository,
                                              RegistroAuditoriaMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public RegistroAuditoria save(RegistroAuditoria registro) {
        if (registro.getId() == null) registro.setId(IdGeneratorUtil.generarId());
        return mapper.toDomain(jpaRepository.save(mapper.toEntity(registro)));
    }

    @Override
    public List<RegistroAuditoria> findAll() {
        return jpaRepository.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
}
