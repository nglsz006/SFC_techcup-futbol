package edu.dosw.project.SFC_TechUp_Futbol.controller;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Arbitro;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.Partido;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.Usuario;
import edu.dosw.project.SFC_TechUp_Futbol.core.service.ArbitroService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "Arbitros", description = "Gestion de arbitros")
@RestController
@RequestMapping("/arbitros")
public class ArbitroController {

    private final ArbitroService arbitroService;

    public ArbitroController(ArbitroService arbitroService) {
        this.arbitroService = arbitroService;
    }

    @Operation(summary = "Crear arbitro")
    @PostMapping
    public Arbitro crearArbitro(@RequestBody Map<String, Object> body) {
        Arbitro arbitro = new Arbitro(
            null,
            body.get("nombre").toString(),
            body.get("email").toString(),
            body.get("password").toString(),
            Usuario.TipoUsuario.valueOf(body.get("tipoUsuario").toString())
        );
        return arbitroService.save(arbitro);
    }

    @Operation(summary = "Consultar partidos asignados al arbitro")
    @GetMapping("/{id}/partidos")
    public List<Partido> consultarPartidosAsignados(@PathVariable Long id) {
        return arbitroService.consultarPartidosAsignados(id);
    }
}
