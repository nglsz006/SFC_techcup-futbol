package edu.dosw.project.SFC_TechUp_Futbol.controller;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Partido;
import edu.dosw.project.SFC_TechUp_Futbol.core.service.PartidoService;
import edu.dosw.project.SFC_TechUp_Futbol.core.validator.PartidoValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Matches", description = "Match management and query. To create a match use the Users endpoints (organizer). To register results, goals and cards use the Users endpoints (referee).")
@RestController
@RequestMapping("/api/partidos")
public class PartidoController {

    private final PartidoService partidoService;
    private final PartidoValidator partidoValidator;

    public PartidoController(PartidoService partidoService, PartidoValidator partidoValidator) {
        this.partidoService = partidoService;
        this.partidoValidator = partidoValidator;
    }

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get match by ID", description = "Returns the full detail of a match: teams, score, goals and cards.")
    @GetMapping("/{id}")
    public Partido consultarPartido(@PathVariable Long id) {
        return partidoService.consultarPartido(id);
    }

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get matches by tournament", description = "Lists all matches in a tournament. Useful for the elimination bracket and standings table.")
    @GetMapping("/torneo/{torneoId}")
    public List<Partido> consultarPorTorneo(@PathVariable Long torneoId) {
        return partidoService.consultarPartidosPorTorneo(torneoId);
    }

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get matches by team", description = "Lists all matches a team has participated in.")
    @GetMapping("/equipo/{equipoId}")
    public List<Partido> consultarPorEquipo(@PathVariable Long equipoId) {
        return partidoService.consultarPartidosPorEquipo(equipoId);
    }
}
