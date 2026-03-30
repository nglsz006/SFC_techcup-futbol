package edu.dosw.project.SFC_TechUp_Futbol.controller;

import edu.dosw.project.SFC_TechUp_Futbol.core.service.AlineacionService;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.Alineacion;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Lineups", description = "Lineup query. To create or view the rival lineup use the Users endpoints (captain).")
@RestController
@RequestMapping("/api/alineaciones")
public class AlineacionController {

    private final AlineacionService service;

    public AlineacionController(AlineacionService service) {
        this.service = service;
    }

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get lineup by ID", description = "Returns the lineup registered for a specific match.")
    @GetMapping("/{id}")
    public Alineacion obtenerAlineacion(@PathVariable int id) {
        return service.obtener(id);
    }

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "List all lineups", description = "Returns all lineups registered in the system.")
    @GetMapping
    public List<Alineacion> listarAlineaciones() {
        return service.listar();
    }
}
