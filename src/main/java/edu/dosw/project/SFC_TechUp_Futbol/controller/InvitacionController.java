package edu.dosw.project.SFC_TechUp_Futbol.controller;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Invitacion;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.Jugador;
import edu.dosw.project.SFC_TechUp_Futbol.core.service.InvitacionService;
import edu.dosw.project.SFC_TechUp_Futbol.core.service.JugadorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "Invitations", description = "Team invitation management.")
@RestController
@RequestMapping("/api/invitations")
public class InvitacionController {

    private final InvitacionService invitacionService;
    private final JugadorService jugadorService;

    public InvitacionController(InvitacionService invitacionService, JugadorService jugadorService) {
        this.invitacionService = invitacionService;
        this.jugadorService = jugadorService;
    }

    @PreAuthorize("hasRole('CAPITAN')")
    @Operation(summary = "Send invitation to player")
    @PostMapping
    public Invitacion enviar(@RequestBody Map<String, Object> body) {
        String jugadorId = body.get("jugadorId").toString();
        String equipoId = body.get("equipoId").toString();
        Jugador.Posicion posicion = Jugador.Posicion.valueOf(body.get("posicion").toString());
        return invitacionService.enviar(jugadorId, equipoId, posicion);
    }

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get invitations by player")
    @GetMapping("/player/{correo}")
    public List<Invitacion> listarPorJugador(@PathVariable String correo) {
        return invitacionService.listarPorJugador(jugadorService.buscarIdPorEmail(correo));
    }

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get invitations by team")
    @GetMapping("/team/{equipoId}")
    public List<Invitacion> listarPorEquipo(@PathVariable String equipoId) {
        return invitacionService.listarPorEquipo(equipoId);
    }

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Accept invitation")
    @PatchMapping("/{id}/accept")
    public Invitacion aceptar(@PathVariable String id) {
        return invitacionService.aceptar(id);
    }

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Reject invitation")
    @PatchMapping("/{id}/reject")
    public Invitacion rechazar(@PathVariable String id) {
        return invitacionService.rechazar(id);
    }
}
