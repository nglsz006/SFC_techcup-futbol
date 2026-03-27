package edu.dosw.project.SFC_TechUp_Futbol.core.service;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Arbitro;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.Partido;
import edu.dosw.project.SFC_TechUp_Futbol.core.repository.ArbitroRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArbitroService {

    private final ArbitroRepository arbitroRepository;

    public ArbitroService(ArbitroRepository arbitroRepository) {
        this.arbitroRepository = arbitroRepository;
    }

    public Arbitro save(Arbitro arbitro) {
        return arbitroRepository.save(arbitro);
    }

    public List<Partido> consultarPartidosAsignados(Long arbitroId) {
        return arbitroRepository.findById(arbitroId)
                .map(Arbitro::getAssignedMatches)
                .orElse(List.of());
    }

    public List<Arbitro> getArbitros() {
        return arbitroRepository.findAll();
    }
}