package edu.dosw.project.SFC_TechUp_Futbol.core.service;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Arbitro;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.Partido;
import edu.dosw.project.SFC_TechUp_Futbol.core.util.IdGeneratorUtil;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.mapper.ArbitroMapper;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.repository.ArbitroJpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArbitroService {

    private final ArbitroJpaRepository arbitroRepository;
    private final ArbitroMapper mapper;

    public ArbitroService(ArbitroJpaRepository arbitroRepository, ArbitroMapper mapper) {
        this.arbitroRepository = arbitroRepository;
        this.mapper = mapper;
    }

    public Arbitro save(Arbitro arbitro) {
        if (arbitro.getId() == null) arbitro.setId(IdGeneratorUtil.generarId());
        return mapper.toDomain(arbitroRepository.save(mapper.toEntity(arbitro)));
    }

    public List<Partido> consultarPartidosAsignados(String arbitroId) {
        return arbitroRepository.findById(arbitroId)
                .map(e -> mapper.toDomain(e).getAssignedMatches())
                .orElse(List.of());
    }

    public List<Arbitro> getArbitros() {
        return arbitroRepository.findAll().stream().map(mapper::toDomain).toList();
    }
}
