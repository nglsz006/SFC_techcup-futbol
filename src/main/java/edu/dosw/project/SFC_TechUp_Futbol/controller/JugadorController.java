package edu.dosw.project.SFC_TechUp_Futbol.controller;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Jugador;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.Posicion;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.TipoUsuario;
import edu.dosw.project.SFC_TechUp_Futbol.core.service.JugadorService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/jugadores")
public class JugadorController {

    private final JugadorService jugadorService;

    public JugadorController(JugadorService jugadorService) {
        this.jugadorService = jugadorService;
    }

    record JugadorRequest(String nombre, String email, String password, String tipoUsuario, int numeroCamiseta, String posicion, String foto) {}

    @PostMapping
    public Jugador crearJugador(@RequestBody JugadorRequest req) {
        List<Jugador> jugadores = jugadorService.getJugadores();
        Long id = (long) (jugadores.size() + 1);
        Jugador jugador = new Jugador(
            id,
            req.nombre(),
            req.email(),
            req.password(),
            TipoUsuario.valueOf(req.tipoUsuario()),
            req.numeroCamiseta(),
            Posicion.valueOf(req.posicion()),
            true,
            req.foto() != null ? req.foto() : ""
        );
        jugadores.add(jugador);
        return jugador;
    }

    @GetMapping
    public List<Jugador> obtenerJugadores() {
        return jugadorService.getJugadores();
    }

    @PatchMapping("/{id}/aceptarInvitacion")
    public String aceptarInvitacion(@PathVariable Long id) {
        jugadorService.aceptarInvitacion(id);
        return "Invitacion aceptada correctamente";
    }

    @PatchMapping("/{id}/rechazarInvitacion")
    public String rechazarInvitacion(@PathVariable Long id) {
        jugadorService.rechazarInvitacion(id);
        return "Invitacion rechazada correctamente";
    }

    @PatchMapping("/{id}/disponibilidad")
    public String marcarDisponible(@PathVariable Long id) {
        jugadorService.marcarDisponible(id);
        return "Jugador marcado como disponible";
    }
}
