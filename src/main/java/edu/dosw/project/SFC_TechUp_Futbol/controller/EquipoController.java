package edu.dosw.project.SFC_TechUp_Futbol.controller;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Equipo;
import edu.dosw.project.SFC_TechUp_Futbol.core.service.EquipoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Teams", description = "Team query. To create a team or add players use the Users endpoints (captain).")
@RestController
@RequestMapping("/api/equipos")
public class EquipoController {

    private final EquipoService service;

    public EquipoController(EquipoService service) {
        this.service = service;
    }

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get team by ID", description = "Returns the information of a team, including its players.")
    @GetMapping("/{id}")
    public Equipo obtenerEquipo(@PathVariable int id) {
        return service.obtener(id);
    }

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "List teams", description = "Returns all teams registered in the tournament.")
    @GetMapping
    public List<Equipo> listarEquipos() {
        return service.listar();
    }
}
