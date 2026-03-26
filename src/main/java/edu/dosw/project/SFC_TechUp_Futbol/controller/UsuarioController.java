package edu.dosw.project.SFC_TechUp_Futbol.controller;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.*;
import edu.dosw.project.SFC_TechUp_Futbol.core.repository.JugadorRepository;
import edu.dosw.project.SFC_TechUp_Futbol.core.repository.PartidoRepository;
import edu.dosw.project.SFC_TechUp_Futbol.core.service.*;
import edu.dosw.project.SFC_TechUp_Futbol.core.validator.PartidoValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;

@Tag(name = "Users", description = "Management of system actors: Players, Captains, Referees and Organizers.")
@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final JugadorService jugadorService;
    private final JugadorRepository jugadorRepository;
    private final CapitanService capitanService;
    private final ArbitroService arbitroService;
    private final PartidoRepository partidoRepository;
    private final OrganizadorService organizadorService;
    private final PagoService pagoService;
    private final PartidoService partidoService;
    private final PartidoValidator partidoValidator;
    private final EquipoService equipoService;
    private final TorneoService torneoService;

    public UsuarioController(JugadorService jugadorService, JugadorRepository jugadorRepository,
                             CapitanService capitanService, ArbitroService arbitroService,
                             PartidoRepository partidoRepository, OrganizadorService organizadorService,
                             PagoService pagoService, PartidoService partidoService,
                             PartidoValidator partidoValidator, EquipoService equipoService,
                             TorneoService torneoService) {
        this.jugadorService = jugadorService;
        this.jugadorRepository = jugadorRepository;
        this.capitanService = capitanService;
        this.arbitroService = arbitroService;
        this.partidoRepository = partidoRepository;
        this.organizadorService = organizadorService;
        this.pagoService = pagoService;
        this.partidoService = partidoService;
        this.partidoValidator = partidoValidator;
        this.equipoService = equipoService;
        this.torneoService = torneoService;
    }

    // ── Jugadores ──────────────────────────────────────────────────────────────

    @Operation(summary = "Get actions by actor", description = "Returns the available actions for a system actor. Valid actors: jugador, capitan, arbitro, organizador.")
    @GetMapping("/{actor}")
    public Map<String, Object> accionesPorActor(@PathVariable String actor) {
        Map<String, List<String>> acciones = new LinkedHashMap<>();
        acciones.put("jugador", List.of(
            "POST   /api/usuarios/jugadores                        - Crear jugador",
            "GET    /api/usuarios/jugadores                        - Listar jugadores",
            "PATCH  /api/usuarios/jugadores/{id}/perfil            - Editar perfil deportivo",
            "PATCH  /api/usuarios/jugadores/{id}/aceptarInvitacion - Aceptar invitacion",
            "PATCH  /api/usuarios/jugadores/{id}/rechazarInvitacion- Rechazar invitacion",
            "PATCH  /api/usuarios/jugadores/{id}/disponibilidad    - Marcar disponibilidad"
        ));
        acciones.put("capitan", List.of(
            "POST   /api/usuarios/capitanes                              - Crear capitan",
            "GET    /api/usuarios/capitanes                              - Listar capitanes",
            "POST   /api/usuarios/capitanes/{id}/equipo                  - Crear equipo",
            "GET    /api/usuarios/capitanes/{id}/equipo/validar          - Validar composicion del equipo",
            "POST   /api/usuarios/capitanes/{id}/invitar/{jugadorId}     - Invitar jugador",
            "POST   /api/usuarios/capitanes/{id}/alineacion              - Definir alineacion",
            "POST   /api/usuarios/capitanes/{id}/comprobante             - Subir comprobante de pago",
            "GET    /api/usuarios/capitanes/{id}/buscarJugadores         - Buscar jugadores por posicion"
        ));
        acciones.put("arbitro", List.of(
            "POST   /api/admin/usuarios                                        - Registrar arbitro (solo administrador)",
            "GET    /api/usuarios/arbitros                                    - Listar arbitros",
            "POST   /api/usuarios/arbitros/{id}/partidos/{partidoId}          - Asignar arbitro a partido",
            "GET    /api/usuarios/arbitros/{id}/partidos                      - Consultar partidos asignados",
            "PUT    /api/usuarios/arbitros/{id}/partidos/{partidoId}/iniciar   - Iniciar partido",
            "PUT    /api/usuarios/arbitros/{id}/partidos/{partidoId}/resultado - Registrar resultado",
            "PUT    /api/usuarios/arbitros/{id}/partidos/{partidoId}/finalizar - Finalizar partido",
            "POST   /api/usuarios/arbitros/{id}/partidos/{partidoId}/goles     - Registrar goleador",
            "POST   /api/usuarios/arbitros/{id}/partidos/{partidoId}/tarjetas  - Registrar tarjeta"
        ));
        acciones.put("organizador", List.of(
            "POST   /api/admin/usuarios                                                - Registrar organizador (solo administrador)",
            "GET    /api/usuarios/organizadores                                        - Listar organizadores",
            "POST   /api/usuarios/organizadores/{id}/torneo                            - Crear torneo",
            "PATCH  /api/usuarios/organizadores/{id}/torneo/iniciar                    - Iniciar torneo",
            "PATCH  /api/usuarios/organizadores/{id}/torneo/finalizar                  - Finalizar torneo",
            "PATCH  /api/usuarios/organizadores/{id}/torneo/configurar                 - Configurar torneo",
            "POST   /api/usuarios/organizadores/{id}/partidos                          - Crear partido",
            "GET    /api/usuarios/organizadores/{id}/pagos/pendientes                  - Ver pagos pendientes",
            "PUT    /api/usuarios/organizadores/{id}/pagos/{pagoId}/aprobar            - Aprobar pago",
            "PUT    /api/usuarios/organizadores/{id}/pagos/{pagoId}/rechazar           - Rechazar pago"
        ));

        String actorNormalizado = actor.toLowerCase();
        if (!acciones.containsKey(actorNormalizado)) {
            Map<String, Object> error = new LinkedHashMap<>();
            error.put("error", "Actor '" + actor + "' no válido");
            error.put("actoresDisponibles", List.of("jugador", "capitan", "arbitro", "organizador"));
            return error;
        }

        Map<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put("actor", actorNormalizado);
        respuesta.put("acciones", acciones.get(actorNormalizado));
        return respuesta;
    }

    @Operation(summary = "Create player", description = "Registers a new Player in the system with their sports profile (position, jersey number).")
    @PostMapping("/jugadores")
    public Jugador crearJugador(@RequestBody Map<String, Object> body) {
        Long id = (long) (jugadorService.getJugadores().size() + 1);
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

    @Operation(summary = "List players", description = "Returns all registered players. Useful for the Captain when looking for team members.")
    @GetMapping("/jugadores")
    public List<Jugador> listarJugadores() {
        return jugadorService.getJugadores();
    }

    @Operation(summary = "Edit sports profile", description = "The Player updates their position, jersey number, name or photo.")
    @PatchMapping("/jugadores/{id}/perfil")
    public Jugador editarPerfil(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        String nombre = body.getOrDefault("nombre", "").toString();
        int numeroCamiseta = body.containsKey("numeroCamiseta") ? Integer.parseInt(body.get("numeroCamiseta").toString()) : 0;
        Jugador.Posicion posicion = body.containsKey("posicion") ? Jugador.Posicion.valueOf(body.get("posicion").toString()) : null;
        String foto = body.getOrDefault("foto", "").toString();
        return jugadorService.editarPerfil(id, nombre, numeroCamiseta, posicion, foto);
    }

    @Operation(summary = "Accept invitation", description = "The Player accepts the invitation sent by a Captain to join their team.")
    @PatchMapping("/jugadores/{id}/aceptarInvitacion")
    public String aceptarInvitacion(@PathVariable Long id) {
        jugadorService.aceptarInvitacion(id);
        return "Invitacion aceptada correctamente";
    }

    @Operation(summary = "Reject invitation", description = "The Player rejects the invitation from a Captain.")
    @PatchMapping("/jugadores/{id}/rechazarInvitacion")
    public String rechazarInvitacion(@PathVariable Long id) {
        jugadorService.rechazarInvitacion(id);
        return "Invitacion rechazada correctamente";
    }

    @Operation(summary = "Mark availability", description = "The Player indicates they are available to be recruited by a team.")
    @PatchMapping("/jugadores/{id}/disponibilidad")
    public String marcarDisponible(@PathVariable Long id) {
        jugadorService.marcarDisponible(id);
        return "Jugador marcado como disponible";
    }

    // ── Capitanes ──────────────────────────────────────────────────────────────

    @Operation(summary = "Create captain", description = "Registers a new Captain. The Captain is a Player with permissions to manage a team.")
    @PostMapping("/capitanes")
    public Capitan crearCapitan(@RequestBody Map<String, Object> body) {
        Usuario.TipoUsuario tipoUsuario;
        Jugador.Posicion posicion;
        try {
            tipoUsuario = Usuario.TipoUsuario.valueOf(body.get("tipoUsuario").toString());
            posicion = Jugador.Posicion.valueOf(body.get("posicion").toString());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Valor de tipoUsuario o posicion no valido");
        }
        Capitan capitan = new Capitan(
            null,
            body.get("nombre").toString(),
            body.get("email").toString(),
            body.get("password").toString(),
            tipoUsuario,
            Integer.parseInt(body.get("numeroCamiseta").toString()),
            posicion,
            true,
            body.getOrDefault("foto", "").toString(),
            null
        );
        return capitanService.save(capitan);
    }

    @Operation(summary = "Validate team composition", description = "The Captain verifies if their team meets the rules: minimum 7 and maximum 12 players.")
    @GetMapping("/capitanes/{id}/equipo/validar")
    public Map<String, Object> validarEquipo(@PathVariable Long id) {
        Capitan capitan = capitanService.getCapitanes().stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Capitán no encontrado"));
        Equipo equipo = equipoService.listar().stream()
                .filter(e -> e.getCapitanId() == capitan.getId().intValue())
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("El capitán no tiene equipo registrado"));
        return equipoService.validarComposicion(equipo.getId());
    }

    @Operation(summary = "List captains", description = "Returns all captains registered in the system.")
    @GetMapping("/capitanes")
    public List<Capitan> listarCapitanes() {
        return capitanService.getCapitanes();
    }

    @Operation(summary = "Create team", description = "The Captain creates their team to register in the tournament.")
    @PostMapping("/capitanes/{id}/equipo")
    public String crearEquipo(@PathVariable Long id, @RequestParam String nombreEquipo) {
        capitanService.crearEquipo(id, nombreEquipo);
        return "Equipo creado correctamente";
    }

    @Operation(summary = "Invite player", description = "The Captain sends an invitation to an available Player to join their team.")
    @PostMapping("/capitanes/{id}/invitar/{jugadorId}")
    public String invitarJugador(@PathVariable Long id, @PathVariable Long jugadorId) {
        capitanService.invitarJugador(id, jugadorId);
        return "Invitacion enviada correctamente";
    }

    @Operation(summary = "Define lineup", description = "The Captain defines the starting players for the next match.")
    @PostMapping("/capitanes/{id}/alineacion")
    public String definirAlineacion(@PathVariable Long id, @RequestBody List<Jugador> titulares) {
        return capitanService.definirAlineacion(id, titulares);
    }

    @Operation(summary = "Upload payment receipt", description = "The Captain uploads the registration payment receipt for their team.")
    @PostMapping("/capitanes/{id}/comprobante")
    public String subirComprobante(@PathVariable Long id, @RequestParam String comprobante) {
        return capitanService.subirComprobantePago(id, comprobante);
    }

    @Operation(summary = "Search players by position", description = "The Captain searches for available players filtering by position (PORTERO, DEFENSA, MEDIOCAMPISTA, DELANTERO).")
    @GetMapping("/capitanes/{id}/buscarJugadores")
    public List<Jugador> buscarJugadores(@PathVariable Long id, @RequestParam String posicion) {
        return capitanService.buscarJugadores(posicion);
    }

    // ── Arbitros ───────────────────────────────────────────────────────────────

    @Operation(summary = "List referees", description = "Returns all available referees in the system.")
    @GetMapping("/arbitros")
    public List<Arbitro> listarArbitros() {
        return arbitroService.getArbitros();
    }

    @Operation(summary = "Assign referee to match", description = "The Organizer assigns a Referee to a scheduled match.")
    @PostMapping("/arbitros/{id}/partidos/{partidoId}")
    public String asignarPartido(@PathVariable Long id, @PathVariable Long partidoId) {
        Arbitro arbitro = arbitroService.getArbitros().stream()
                .filter(a -> a.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Arbitro no encontrado"));
        Partido partido = partidoRepository.findById(partidoId)
                .orElseThrow(() -> new IllegalArgumentException("Partido no encontrado"));
        arbitro.getAssignedMatches().add(partido);
        arbitroService.save(arbitro);
        return "Arbitro asignado al partido correctamente";
    }

    @Operation(summary = "Get referee matches", description = "The Referee queries the matches they have been assigned to referee.")
    @GetMapping("/arbitros/{id}/partidos")
    public List<Partido> consultarPartidosAsignados(@PathVariable Long id) {
        return arbitroService.consultarPartidosAsignados(id);
    }

    @Operation(summary = "Start match", description = "The Referee changes the match status to EN_CURSO.")
    @PutMapping("/arbitros/{id}/partidos/{partidoId}/iniciar")
    public Partido iniciarPartido(@PathVariable Long id, @PathVariable Long partidoId) {
        return partidoService.iniciarPartido(partidoId);
    }

    @Operation(summary = "Register result", description = "The Referee registers the match score.")
    @PutMapping("/arbitros/{id}/partidos/{partidoId}/resultado")
    public Partido registrarResultado(@PathVariable Long id, @PathVariable Long partidoId,
                                      @RequestBody Map<String, Integer> body) {
        partidoValidator.validarResultado(body.get("golesLocal"), body.get("golesVisitante"));
        return partidoService.registrarResultado(partidoId, body.get("golesLocal"), body.get("golesVisitante"));
    }

    @Operation(summary = "End match", description = "The Referee closes the match and marks it as FINALIZADO.")
    @PutMapping("/arbitros/{id}/partidos/{partidoId}/finalizar")
    public Partido finalizarPartido(@PathVariable Long id, @PathVariable Long partidoId) {
        return partidoService.finalizarPartido(partidoId);
    }

    @Operation(summary = "Register goal scorer", description = "The Referee registers a goal indicating the player and the minute.")
    @PostMapping("/arbitros/{id}/partidos/{partidoId}/goles")
    public Partido registrarGoleador(@PathVariable Long id, @PathVariable Long partidoId,
                                     @RequestBody Map<String, Object> body) {
        Long jugadorId = Long.valueOf(body.get("jugadorId").toString());
        int minuto = Integer.parseInt(body.get("minuto").toString());
        partidoValidator.validarGoleador(jugadorId, minuto);
        return partidoService.registrarGoleador(partidoId, jugadorId, minuto);
    }

    @Operation(summary = "Register card", description = "The Referee registers a card (AMARILLA or ROJA) for a player.")
    @PostMapping("/arbitros/{id}/partidos/{partidoId}/tarjetas")
    public Partido registrarTarjeta(@PathVariable Long id, @PathVariable Long partidoId,
                                    @RequestBody Map<String, Object> body) {
        Long jugadorId = Long.valueOf(body.get("jugadorId").toString());
        Partido.Tarjeta.TipoTarjeta tipo = Partido.Tarjeta.TipoTarjeta.valueOf(body.get("tipo").toString());
        int minuto = Integer.parseInt(body.get("minuto").toString());
        partidoValidator.validarTarjeta(jugadorId, tipo, minuto);
        return partidoService.registrarTarjeta(partidoId, jugadorId, tipo, minuto);
    }

    // ── Organizadores ──────────────────────────────────────────────────────────

    @Operation(summary = "List organizers", description = "Returns all organizers registered in the system.")
    @GetMapping("/organizadores")
    public List<Organizador> listarOrganizadores() {
        return organizadorService.getOrganizadores();
    }

    @Operation(summary = "Create tournament", description = "The Organizer creates a new tournament with name, dates, number of teams and registration cost.")
    @PostMapping("/organizadores/{id}/torneo")
    public Torneo crearTorneo(@PathVariable Long id, @RequestBody Torneo torneo) {
        return organizadorService.crearTorneo(id, torneo);
    }

    @Operation(summary = "Start tournament", description = "The Organizer changes the tournament status to EN_CURSO, enabling match registration.")
    @PatchMapping("/organizadores/{id}/torneo/iniciar")
    public Torneo iniciarTorneo(@PathVariable Long id) {
        return organizadorService.iniciarTorneo(id);
    }

    @Operation(summary = "End tournament", description = "The Organizer closes the tournament and marks it as FINALIZADO.")
    @PatchMapping("/organizadores/{id}/torneo/finalizar")
    public Torneo finalizarTorneo(@PathVariable Long id) {
        return organizadorService.finalizarTorneo(id);
    }

    @Operation(summary = "View pending payments", description = "The Organizer queries all payment receipts that have not yet been verified.")
    @GetMapping("/organizadores/{id}/pagos/pendientes")
    public List<Pago> pagosPendientes(@PathVariable Long id) {
        return pagoService.consultarPagosPendientes();
    }

    @Operation(summary = "Approve payment", description = "The Organizer approves a team's payment receipt, confirming their registration.")
    @PutMapping("/organizadores/{id}/pagos/{pagoId}/aprobar")
    public Pago aprobarPago(@PathVariable Long id, @PathVariable Long pagoId) {
        return pagoService.aprobarPago(pagoId);
    }

    @Operation(summary = "Configure tournament", description = "The Organizer defines the regulations, courts, schedules, sanctions and registration closing date.")
    @PatchMapping("/organizadores/{id}/torneo/configurar")
    public Torneo configurarTorneo(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        Torneo torneoActual = organizadorService.getOrganizadores().stream()
                .filter(o -> o.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Organizador no encontrado"))
                .getCurrentTournament();
        if (torneoActual == null) throw new IllegalStateException("El organizador no tiene torneo activo");
        LocalDateTime cierre = body.containsKey("cierreInscripciones")
                ? LocalDateTime.parse(body.get("cierreInscripciones").toString()) : null;
        return torneoService.configurar(
                torneoActual.getId(),
                body.getOrDefault("reglamento", "").toString(),
                body.getOrDefault("canchas", "").toString(),
                body.getOrDefault("horarios", "").toString(),
                body.getOrDefault("sanciones", "").toString(),
                cierre
        );
    }

    @Operation(summary = "Reject payment", description = "The Organizer rejects a team's payment receipt for being invalid or incomplete.")
    @PutMapping("/organizadores/{id}/pagos/{pagoId}/rechazar")
    public Pago rechazarPago(@PathVariable Long id, @PathVariable Long pagoId) {
        return pagoService.rechazarPago(pagoId);
    }

    @Operation(summary = "Create match", description = "The Organizer schedules a match between two teams within a tournament.")
    @PostMapping("/organizadores/{id}/partidos")
    public Partido crearPartido(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        Long torneoId = Long.valueOf(body.get("torneoId").toString());
        Long equipoLocalId = Long.valueOf(body.get("equipoLocalId").toString());
        Long equipoVisitanteId = Long.valueOf(body.get("equipoVisitanteId").toString());
        java.time.LocalDateTime fecha = java.time.LocalDateTime.parse(body.get("fecha").toString());
        String cancha = body.get("cancha").toString();
        partidoValidator.validarCrearPartido(torneoId, equipoLocalId, equipoVisitanteId, fecha, cancha);
        return partidoService.crearPartido(torneoId, equipoLocalId, equipoVisitanteId, fecha, cancha);
    }

    @PostMapping("/jugadores/{id}/foto")
    public String subirFotoJugador(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        return jugadorService.subirFoto(id, file);
    }
}



