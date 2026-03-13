package edu.dosw.project.SFC_TechUp_Futbol.controller;

import edu.dosw.project.SFC_TechUp_Futbol.model.Partido;
import edu.dosw.project.SFC_TechUp_Futbol.service.ArbitroService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/arbitros")
public class ArbitroController {

    private final ArbitroService arbitroService = new ArbitroService();

    @GetMapping("/{id}/partidos")
    public List<Partido> consultarPartidosAsignados(@PathVariable Long id) {
        return arbitroService.consultarPartidosAsignados(id);
    }

    @GetMapping("/{id}/equipos")
    public void consultarEquipos(@PathVariable Long id) {
        arbitroService.consultarEquipos(id);
    }

    @GetMapping("/{id}/fechaHora")
    public void consultarFechaHora(@PathVariable Long id) {
        arbitroService.consultarFechaHora(id);
    }

    @GetMapping("/{id}/cancha")
    public void consultarCancha(@PathVariable Long id) {
        arbitroService.consultarCancha(id);
    }
}
