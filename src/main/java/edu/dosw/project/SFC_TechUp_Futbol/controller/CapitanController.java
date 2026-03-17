package edu.dosw.project.SFC_TechUp_Futbol.controller;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Capitan;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.Jugador;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.Usuario;
import edu.dosw.project.SFC_TechUp_Futbol.core.service.CapitanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "Capitanes", description = "Gestion de capitanes")
@RestController
@RequestMapping("/capitanes")
public class CapitanController {

    private final CapitanService capitanService;

    public CapitanController(CapitanService capitanService) {
        this.capitanService = capitanService;
    }

    @Operation(summary = "Crear capitan")
    @PostMapping
    public Capitan crearCapitan(@RequestBody Map<String, Object> body) {
        Capitan capitan = new Capitan(
            null,
            body.get("nombre").toString(),
            body.get("email").toString(),
            body.get("password").toString(),
            Usuario.TipoUsuario.valueOf(body.get("tipoUsuario").toString()),
            Integer.parseInt(body.get("numeroCamiseta").toString()),
            Jugador.Posicion.valueOf(body.get("posicion").toString()),
            true,
            body.getOrDefault("foto", "").toString(),
            null
        );
        return capitanService.save(capitan);
    }

    @Operation(summary = "Crear equipo")
    @PostMapping("/{id}/equipo")
    public String crearEquipo(@PathVariable Long id, @RequestParam String nombreEquipo) {
        return capitanService.crearEquipo(id, nombreEquipo);
    }

    @Operation(summary = "Invitar jugador")
    @PostMapping("/{id}/invitar/{jugadorId}")
    public String invitarJugador(@PathVariable Long id, @PathVariable Long jugadorId) {
        return capitanService.invitarJugador(id, jugadorId);
    }

    @Operation(summary = "Definir alineacion")
    @PostMapping("/{id}/alineacion")
    public String definirAlineacion(@PathVariable Long id, @RequestBody List<Jugador> titulares) {
        return capitanService.definirAlineacion(id, titulares);
    }

    @Operation(summary = "Subir comprobante de pago")
    @PostMapping("/{id}/comprobante")
    public String subirComprobante(@PathVariable Long id, @RequestParam String comprobante) {
        return capitanService.subirComprobantePago(id, comprobante);
    }

    @Operation(summary = "Buscar jugadores por posicion")
    @GetMapping("/{id}/buscarJugadores")
    public List<Jugador> buscarJugadores(@PathVariable Long id, @RequestParam String posicion) {
        return capitanService.buscarJugadores(posicion);
    }
}
