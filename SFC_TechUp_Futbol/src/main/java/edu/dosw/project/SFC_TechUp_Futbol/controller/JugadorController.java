package edu.dosw.project.SFC_TechUp_Futbol.controller;

import edu.dosw.project.SFC_TechUp_Futbol.model.Jugador;
import edu.dosw.project.SFC_TechUp_Futbol.service.JugadorService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/jugadores")
public class JugadorController {

    private final JugadorService jugadorService = new JugadorService();

    @PatchMapping("/{id}/aceptarInvitacion")
    public String aceptarInvitacion(@PathVariable Long id) {
        jugadorService.aceptarInvitacion(id);
        return "Invitación aceptada correctamente";
    }

    @PatchMapping("/{id}/rechazarInvitacion")
    public String rechazarInvitacion(@PathVariable Long id) {
        jugadorService.rechazarInvitacion(id);
        return "Invitación rechazada correctamente";
    }

    @PatchMapping("/{id}/disponibilidad")
    public String marcarDisponible(@PathVariable Long id) {
        jugadorService.marcarDisponible(id);
        return "Jugador marcado como disponible";
    }

    @GetMapping
    public List<Jugador> obtenerJugadores() {
        return jugadorService.getJugadores();
    }
}
