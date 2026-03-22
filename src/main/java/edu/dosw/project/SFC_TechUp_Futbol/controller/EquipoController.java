package edu.dosw.project.SFC_TechUp_Futbol.controller;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Equipo;
import edu.dosw.project.SFC_TechUp_Futbol.core.service.EquipoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Equipos", description = "Consulta de equipos. Para crear un equipo o agregar jugadores usa los endpoints de Usuarios (capitán).")
@RestController
@RequestMapping("/api/equipos")
public class EquipoController {

    private final EquipoService service;

    public EquipoController(EquipoService service) {
        this.service = service;
    }

    @Operation(summary = "Obtener equipo por ID", description = "Retorna la información de un equipo, incluyendo sus jugadores.")
    @GetMapping("/{id}")
    public Equipo obtenerEquipo(@PathVariable int id) {
        return service.obtener(id);
    }

    @Operation(summary = "Listar equipos", description = "Retorna todos los equipos registrados en el torneo.")
    @GetMapping
    public List<Equipo> listarEquipos() {
        return service.listar();
    }
}
