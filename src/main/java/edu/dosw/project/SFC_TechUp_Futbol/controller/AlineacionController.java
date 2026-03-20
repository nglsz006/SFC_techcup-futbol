package edu.dosw.project.SFC_TechUp_Futbol.controller;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Alineacion;
import edu.dosw.project.SFC_TechUp_Futbol.core.service.AlineacionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "Alineaciones", description = "Gestion de alineaciones")
@RestController
@RequestMapping("/api/alineaciones")
public class AlineacionController {

    private final AlineacionService service;

    public AlineacionController(AlineacionService service) {
        this.service = service;
    }

    record AlineacionRequest(int equipoId, int partidoId, String formacion, List<Integer> titulares, List<Integer> reservas) {}

    @Operation(summary = "Crear alineacion")
    @PostMapping
    public Alineacion crearAlineacion(@RequestBody AlineacionRequest req) {
        Alineacion alineacion = new Alineacion();
        alineacion.setEquipoId(req.equipoId());
        alineacion.setPartidoId(req.partidoId());
        alineacion.setFormacion(Alineacion.Formacion.fromString(req.formacion()));
        alineacion.setTitulares(req.titulares() != null ? req.titulares() : List.of());
        alineacion.setReservas(req.reservas() != null ? req.reservas() : List.of());
        return service.crear(alineacion, Map.of());
    }

    @Operation(summary = "Obtener alineacion por id")
    @GetMapping("/{id}")
    public Alineacion obtenerAlineacion(@PathVariable int id) {
        return service.obtener(id);
    }
}
