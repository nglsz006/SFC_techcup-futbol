package edu.dosw.project.SFC_TechUp_Futbol.persistence.repository;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.UsuarioRegistrado;
import edu.dosw.project.SFC_TechUp_Futbol.core.repository.UsuarioRegistradoRepository;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.mapper.UsuarioRegistradoMapper;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import edu.dosw.project.SFC_TechUp_Futbol.core.util.IdGeneratorUtil;
import java.util.stream.Collectors;

@Primary
@Repository
public class UsuarioRegistradoRepositoryJpaImpl implements UsuarioRegistradoRepository {

    private final UsuarioRegistradoJpaRepository jpaRepository;
    private final UsuarioRegistradoMapper mapper;

    public UsuarioRegistradoRepositoryJpaImpl(UsuarioRegistradoJpaRepository jpaRepository,
                                              UsuarioRegistradoMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public UsuarioRegistrado save(UsuarioRegistrado usuario) {
        if (usuario.getId() == null) usuario.setId(IdGeneratorUtil.generarId());
        return mapper.toDomain(jpaRepository.save(mapper.toEntity(usuario)));
    }

    @Override
    public Optional<UsuarioRegistrado> findByEmail(String email) {
        return jpaRepository.findByEmail(email).map(mapper::toDomain);
    }

    @Override
    public List<UsuarioRegistrado> findAll() {
        return jpaRepository.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
}
