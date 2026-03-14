package edu.dosw.project.SFC_TechUp_Futbol.controller;

import edu.dosw.project.SFC_TechUp_Futbol.core.service.AlineacionService;
import edu.dosw.project.SFC_TechUp_Futbol.core.service.NotificadorTorneo;
import edu.dosw.project.SFC_TechUp_Futbol.core.service.LoggerObserver;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.Alineacion;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.Formacion;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/alineaciones")
public class AlineacionController {

    private final AlineacionService service;

    public AlineacionController(AlineacionService service) {
        this.service = service;
    }

    record AlineacionRequest(int equipoId, int partidoId, String formacion, List<Integer> titulares, List<Integer> reservas) {}

    @PostMapping
    public Alineacion crearAlineacion(@RequestBody AlineacionRequest req) {
        Alineacion alineacion = new Alineacion();
        alineacion.setEquipoId(req.equipoId());
        alineacion.setPartidoId(req.partidoId());
        alineacion.setFormacion(Formacion.fromString(req.formacion()));
        alineacion.setTitulares(req.titulares() != null ? req.titulares() : List.of());
        alineacion.setReservas(req.reservas() != null ? req.reservas() : List.of());
        return service.crear(alineacion, Map.of());
    }

    @GetMapping("/{id}")
    public Alineacion obtenerAlineacion(@PathVariable int id) {
        return service.obtener(id);
    }
}
