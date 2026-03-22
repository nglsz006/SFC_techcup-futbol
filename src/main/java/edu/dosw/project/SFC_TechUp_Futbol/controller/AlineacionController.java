package edu.dosw.project.SFC_TechUp_Futbol.controller;

import edu.dosw.project.SFC_TechUp_Futbol.core.service.AlineacionService;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.Alineacion;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Alineaciones", description = "Consulta de alineaciones. Para crear o consultar la alineación rival usa los endpoints de Usuarios (capitán).")
@RestController
@RequestMapping("/api/alineaciones")
public class AlineacionController {

    private final AlineacionService service;

    public AlineacionController(AlineacionService service) {
        this.service = service;
    }

    @Operation(summary = "Obtener alineación por ID", description = "Retorna la alineación registrada para un partido específico.")
    @GetMapping("/{id}")
    public Alineacion obtenerAlineacion(@PathVariable int id) {
        return service.obtener(id);
    }

    @Operation(summary = "Listar todas las alineaciones", description = "Retorna todas las alineaciones registradas en el sistema.")
    @GetMapping
    public List<Alineacion> listarAlineaciones() {
        return service.listar();
    }
}
