package edu.dosw.project.SFC_TechUp_Futbol.controller;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Jugador;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.Usuario;
import edu.dosw.project.SFC_TechUp_Futbol.core.repository.JugadorRepository;
import edu.dosw.project.SFC_TechUp_Futbol.core.service.JugadorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "Jugadores", description = "Gestion de jugadores")
@RestController
@RequestMapping("/api/jugadores")
public class JugadorController {

    private final JugadorService jugadorService;
    private final JugadorRepository jugadorRepository;

    public JugadorController(JugadorService jugadorService, JugadorRepository jugadorRepository) {
        this.jugadorService = jugadorService;
        this.jugadorRepository = jugadorRepository;
    }

    @Operation(summary = "Crear jugador")
    @PostMapping
    public Jugador crearJugador(@RequestBody Map<String, Object> body) {
        List<Jugador> jugadores = jugadorService.getJugadores();
        Long id = (long) (jugadores.size() + 1);
        Jugador jugador = new Jugador(
            id,
            body.get("nombre").toString(),
            body.get("email").toString(),
            body.get("password").toString(),
            Usuario.TipoUsuario.valueOf(body.get("tipoUsuario").toString()),
            Integer.parseInt(body.get("numeroCamiseta").toString()),
            Jugador.Posicion.valueOf(body.get("posicion").toString()),
            true,
            body.getOrDefault("foto", "").toString()
        );
        return jugadorRepository.save(jugador);
    }

    @Operation(summary = "Listar jugadores")
    @GetMapping
    public List<Jugador> obtenerJugadores() {
        return jugadorService.getJugadores();
    }

    @Operation(summary = "Aceptar invitacion")
    @PatchMapping("/{id}/aceptarInvitacion")
    public String aceptarInvitacion(@PathVariable Long id) {
        jugadorService.aceptarInvitacion(id);
        return "Invitacion aceptada correctamente";
    }

    @Operation(summary = "Rechazar invitacion")
    @PatchMapping("/{id}/rechazarInvitacion")
    public String rechazarInvitacion(@PathVariable Long id) {
        jugadorService.rechazarInvitacion(id);
        return "Invitacion rechazada correctamente";
    }

    @Operation(summary = "Marcar jugador como disponible")
    @PatchMapping("/{id}/disponibilidad")
    public String marcarDisponible(@PathVariable Long id) {
        jugadorService.marcarDisponible(id);
        return "Jugador marcado como disponible";
    }
}
