package edu.dosw.project.SFC_TechUp_Futbol.controller;

import edu.dosw.project.SFC_TechUp_Futbol.model.Torneo;
import edu.dosw.project.SFC_TechUp_Futbol.service.OrganizadorService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/organizadores")
public class OrganizadorController {

    private final OrganizadorService organizadorService = new OrganizadorService();

    @PostMapping("/{id}/torneo")
    public String crearTorneo(@PathVariable Long id, @RequestBody Torneo torneo) {
        organizadorService.crearTorneo(id, torneo);
        return "Torneo creado correctamente en estado BORRADOR";
    }

    @PatchMapping("/{id}/torneo/configurar")
    public String configurarTorneo(@PathVariable Long id, @RequestParam String configuracion) {
        organizadorService.configurarTorneo(id, configuracion);
        return "Torneo configurado correctamente";
    }

    @PatchMapping("/{id}/torneo/iniciar")
    public String iniciarTorneo(@PathVariable Long id) {
        organizadorService.iniciarTorneo(id);
        return "Torneo iniciado correctamente";
    }

    @PatchMapping("/{id}/torneo/cerrarInscripciones")
    public String cerrarInscripciones(@PathVariable Long id) {
        organizadorService.cerrarInscripciones(id);
        return "Inscripciones cerradas correctamente";
    }

    @PatchMapping("/{id}/resultado/{partidoId}")
    public String registrarResultado(@PathVariable Long id,
                                     @PathVariable Long partidoId,
                                     @RequestParam String resultado) {
        organizadorService.registrarResultado(id, partidoId, resultado);
        return "Resultado registrado correctamente";
    }

    @PatchMapping("/{id}/pago/{equipoId}")
    public String verificarPago(@PathVariable Long id,
                                @PathVariable Long equipoId,
                                @RequestParam boolean aprobado) {
        organizadorService.verificarPago(id, equipoId, aprobado);
        return aprobado ? "Pago aprobado" : "Pago rechazado";
    }
}