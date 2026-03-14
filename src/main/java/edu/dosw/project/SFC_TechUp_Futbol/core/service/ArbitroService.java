package edu.dosw.project.SFC_TechUp_Futbol.core.service;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Arbitro;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.Partido;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class ArbitroService {

    private List<Arbitro> arbitros = new ArrayList<>();

    public List<Partido> consultarPartidosAsignados(Long arbitroId) {
        Arbitro arbitro = buscarArbitroPorId(arbitroId);
        if (arbitro != null) {
            return arbitro.getAssignedMatches();
        }
        return new ArrayList<>();
    }

    public void consultarEquipos(Long arbitroId) {
        List<Partido> partidos = consultarPartidosAsignados(arbitroId);
        for (Partido partido : partidos) {
            System.out.println("partido: " + partido.getId());
        }
    }

    public void consultarFechaHora(Long arbitroId) {
        List<Partido> partidos = consultarPartidosAsignados(arbitroId);
        for (Partido partido : partidos) {
            System.out.println("fecha y hora: " + partido.getFecha());
        }
    }

    public void consultarCancha(Long arbitroId) {
        List<Partido> partidos = consultarPartidosAsignados(arbitroId);
        for (Partido partido : partidos) {
            System.out.println("cancha: " + partido.getCancha());
        }
    }

    public Arbitro buscarArbitroPorId(Long id) {
        for (Arbitro arbitro : arbitros) {
            if (arbitro.getId().equals(id)) {
                return arbitro;
            }
        }
        return null;
    }

    public List<Arbitro> getArbitros() {
        return arbitros;
    }
}

