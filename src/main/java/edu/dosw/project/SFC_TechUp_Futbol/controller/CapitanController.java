package edu.dosw.project.SFC_TechUp_Futbol.controller;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Jugador;
import edu.dosw.project.SFC_TechUp_Futbol.core.service.CapitanService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/capitanes")
public class CapitanController {

    private final CapitanService capitanService;

    public CapitanController(CapitanService capitanService) {
        this.capitanService = capitanService;
    }

    @PostMapping("/{id}/equipo")
    public String crearEquipo(@PathVariable Long id, @RequestParam String nombreEquipo) {
        return capitanService.crearEquipo(id, nombreEquipo);
    }

    @PostMapping("/{id}/invitar/{jugadorId}")
    public String invitarJugador(@PathVariable Long id, @PathVariable Long jugadorId) {
        return capitanService.invitarJugador(id, jugadorId);
    }

    @PostMapping("/{id}/alineacion")
    public String definirAlineacion(@PathVariable Long id, @RequestBody List<Jugador> titulares) {
        return capitanService.definirAlineacion(id, titulares);
    }

    @PostMapping("/{id}/comprobante")
    public String subirComprobante(@PathVariable Long id, @RequestParam String comprobante) {
        return capitanService.subirComprobantePago(id, comprobante);
    }

    @GetMapping("/{id}/buscarJugadores")
    public List<Jugador> buscarJugadores(@PathVariable Long id, @RequestParam String posicion) {
        return capitanService.buscarJugadores(posicion);
    }
}
