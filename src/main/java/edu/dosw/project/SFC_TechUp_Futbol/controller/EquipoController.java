package edu.dosw.project.SFC_TechUp_Futbol.controller;

import edu.dosw.project.SFC_TechUp_Futbol.controller.dto.response.EquipoResponse;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.Equipo;
import edu.dosw.project.SFC_TechUp_Futbol.core.service.EquipoService;
import edu.dosw.project.SFC_TechUp_Futbol.core.service.JugadorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "Teams", description = "Team management: query and player association.")
@RestController
@RequestMapping("/api/teams")
public class EquipoController {

    private final EquipoService service;
    private final JugadorService jugadorService;

    public EquipoController(EquipoService service, JugadorService jugadorService) {
        this.service = service;
        this.jugadorService = jugadorService;
    }

    @PreAuthorize("hasRole('CAPITAN')")
    @Operation(summary = "Create team", description = "Registers a new team in the system.")
    @PostMapping
    public EquipoResponse crearEquipo(@RequestBody Map<String, Object> body) {
        Equipo equipo = new Equipo();
        equipo.setNombre(body.getOrDefault("nombre", "").toString());
        equipo.setEscudo(body.getOrDefault("escudo", "").toString());
        equipo.setColorPrincipal(body.getOrDefault("colorPrincipal", "").toString());
        equipo.setColorSecundario(body.getOrDefault("colorSecundario", "").toString());
        equipo.setCapitanId(body.getOrDefault("capitanId", "").toString());
        return new EquipoResponse(service.crear(equipo, body));
    }

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get team by ID", description = "Returns the information of a team, including its players.")
    @GetMapping("/{id}")
    public EquipoResponse obtenerEquipo(@PathVariable String id) {
        return new EquipoResponse(service.obtener(id));
    }

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "List teams", description = "Returns all teams registered in the tournament.")
    @GetMapping
    public List<EquipoResponse> listarEquipos() {
        return service.listar().stream().map(EquipoResponse::new).toList();
    }

    @PreAuthorize("hasRole('CAPITAN')")
    @Operation(summary = "Add player to team", description = "Associates an existing player to a team.")
    @PostMapping("/{equipoId}/jugadores/{jugadorId}")
    public EquipoResponse agregarJugador(@PathVariable String equipoId, @PathVariable String jugadorId) {
        if (jugadorService.buscarJugadorPorId(jugadorId) == null)
            throw new IllegalArgumentException("Jugador no encontrado.");
        return new EquipoResponse(service.agregarJugador(equipoId, jugadorId));
    }
}
