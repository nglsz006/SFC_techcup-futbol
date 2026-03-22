package edu.dosw.project.SFC_TechUp_Futbol.controller;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Partido;
import edu.dosw.project.SFC_TechUp_Futbol.core.service.PartidoService;
import edu.dosw.project.SFC_TechUp_Futbol.core.validator.PartidoValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Partidos", description = "Gestión y consulta de partidos. Para crear un partido usa los endpoints de Usuarios (organizador). Para registrar resultados, goles y tarjetas usa los endpoints de Usuarios (árbitro).")
@RestController
@RequestMapping("/api/partidos")
public class PartidoController {

    private final PartidoService partidoService;
    private final PartidoValidator partidoValidator;

    public PartidoController(PartidoService partidoService, PartidoValidator partidoValidator) {
        this.partidoService = partidoService;
        this.partidoValidator = partidoValidator;
    }

    @Operation(summary = "Consultar partido por ID", description = "Retorna el detalle completo de un partido: equipos, marcador, goles y tarjetas.")
    @GetMapping("/{id}")
    public Partido consultarPartido(@PathVariable Long id) {
        return partidoService.consultarPartido(id);
    }

    @Operation(summary = "Consultar partidos por torneo", description = "Lista todos los partidos de un torneo. Útil para la llave eliminatoria y tabla de posiciones.")
    @GetMapping("/torneo/{torneoId}")
    public List<Partido> consultarPorTorneo(@PathVariable Long torneoId) {
        return partidoService.consultarPartidosPorTorneo(torneoId);
    }

    @Operation(summary = "Consultar partidos por equipo", description = "Lista todos los partidos en los que ha participado un equipo.")
    @GetMapping("/equipo/{equipoId}")
    public List<Partido> consultarPorEquipo(@PathVariable Long equipoId) {
        return partidoService.consultarPartidosPorEquipo(equipoId);
    }
}
