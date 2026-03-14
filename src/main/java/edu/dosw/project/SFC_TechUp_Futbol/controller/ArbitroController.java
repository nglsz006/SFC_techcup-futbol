package edu.dosw.project.SFC_TechUp_Futbol.controller;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Partido;
import edu.dosw.project.SFC_TechUp_Futbol.core.service.ArbitroService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/arbitros")
public class ArbitroController {

    private final ArbitroService arbitroService;

    public ArbitroController(ArbitroService arbitroService) {
        this.arbitroService = arbitroService;
    }

    @GetMapping("/{id}/partidos")
    public List<Partido> consultarPartidosAsignados(@PathVariable Long id) {
        return arbitroService.consultarPartidosAsignados(id);
    }
}
