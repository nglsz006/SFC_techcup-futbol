package edu.dosw.project.SFC_TechUp_Futbol.controller;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Partido;
import edu.dosw.project.SFC_TechUp_Futbol.core.service.PartidoService;
import edu.dosw.project.SFC_TechUp_Futbol.core.validator.PartidoValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Matches", description = "Match management and query.")
@RestController
@RequestMapping("/api/matches")
public class PartidoController {

    private final PartidoService partidoService;
    private final PartidoValidator partidoValidator;

    public PartidoController(PartidoService partidoService, PartidoValidator partidoValidator) {
        this.partidoService = partidoService;
        this.partidoValidator = partidoValidator;
    }

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get match by ID")
    @GetMapping("/{id}")
    public Partido consultarPartido(@PathVariable String id) {
        return partidoService.consultarPartido(id);
    }

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get matches by tournament")
    @GetMapping("/tournament/{tournamentId}")
    public List<Partido> consultarPorTorneo(@PathVariable String tournamentId) {
        return partidoService.consultarPartidosPorTorneo(tournamentId);
    }

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get matches by team")
    @GetMapping("/team/{teamId}")
    public List<Partido> consultarPorEquipo(@PathVariable String teamId) {
        return partidoService.consultarPartidosPorEquipo(teamId);
    }
}
