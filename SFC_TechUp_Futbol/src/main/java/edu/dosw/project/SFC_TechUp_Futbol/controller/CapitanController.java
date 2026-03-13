package edu.dosw.project.SFC_TechUp_Futbol.controller;

import edu.dosw.project.SFC_TechUp_Futbol.model.Jugador;
import edu.dosw.project.SFC_TechUp_Futbol.service.CapitanService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

//Necesidad de implementacion de la dependencia web en el pom pipipipi


@RestController
@RequestMapping("/capitanes")
public class CapitanController {

    private final CapitanService capitanService = new CapitanService();

    @PostMapping("/{id}/equipo")
    public String crearEquipo(@PathVariable Long id, @RequestParam String nombreEquipo) {
        capitanService.crearEquipo(id, nombreEquipo);
        return "Equipo " + nombreEquipo + " creado correctamente";
    }

    @PostMapping("/{id}/invitar/{jugadorId}")
    public String invitarJugador(@PathVariable Long id, @PathVariable Long jugadorId) {
        capitanService.invitarJugador(id, jugadorId);
        return "Invitacion enviada correctamente";
    }

    @PostMapping("/{id}/alineacion")
    public String definirAlineacion(@PathVariable Long id, @RequestBody List<Jugador> titulares) {
        capitanService.definirAlineacion(id, titulares);
        return "Alineacion definida correctamente";
    }

    @PostMapping("/{id}/comprobante")
    public String subirComprobante(@PathVariable Long id, @RequestParam String comprobante) {
        capitanService.subirComprobantePago(id, comprobante);
        return "Comprobante subido correctamente";
    }

    @GetMapping("/{id}/buscarJugadores")
    public List<Jugador> buscarJugadores(@PathVariable Long id, @RequestParam String posicion) {
        return capitanService.buscarJugadores(capitanService.getJugadoresDisponibles(), posicion);
    }
}
