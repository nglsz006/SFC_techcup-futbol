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
@RequestMapping("/api/users")
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
    private final PerfilDeportivoService perfilDeportivoService;

    public UsuarioController(JugadorService jugadorService, JugadorRepository jugadorRepository,
                             CapitanService capitanService, ArbitroService arbitroService,
                             PartidoRepository partidoRepository, OrganizadorService organizadorService,
                             PagoService pagoService, PartidoService partidoService,
                             PartidoValidator partidoValidator, EquipoService equipoService,
                             TorneoService torneoService, PerfilDeportivoService perfilDeportivoService) {
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
        this.perfilDeportivoService = perfilDeportivoService;
    }

    @Operation(summary = "Get actions by actor", description = "Returns the available actions for a system actor. Valid actors: player, captain, referee, organizer.")
    @GetMapping("/{actor}")
    public Map<String, Object> accionesPorActor(@PathVariable String actor) {
        Map<String, List<String>> acciones = new LinkedHashMap<>();
        acciones.put("player", List.of(
                "POST   /api/users/players                          - Create player",
                "GET    /api/users/players                          - List players",
                "POST   /api/users/players/{id}/profile             - Create sports profile",
                "GET    /api/users/players/{id}/profile             - Get sports profile",
                "PATCH  /api/users/players/{id}/profile             - Edit sports profile",
                "PATCH  /api/users/players/{id}/accept-invitation   - Accept invitation",
                "PATCH  /api/users/players/{id}/reject-invitation   - Reject invitation",
                "PATCH  /api/users/players/{id}/availability        - Mark availability"
        ));
        acciones.put("captain", List.of(
                "POST   /api/users/captains                              - Create captain",
                "GET    /api/users/captains                              - List captains",
                "POST   /api/users/captains/{id}/team                    - Create team",
                "GET    /api/users/captains/{id}/team/validate           - Validate team composition",
                "POST   /api/users/captains/{id}/invite/{playerId}       - Invite player",
                "POST   /api/users/captains/{id}/lineup                  - Define lineup",
                "POST   /api/users/captains/{id}/receipt                 - Upload payment receipt",
                "GET    /api/users/captains/{id}/search-players          - Search players by position"
        ));
        acciones.put("referee", List.of(
                "POST   /api/admin/users                                          - Register referee (admin only)",
                "GET    /api/users/referees                                       - List referees",
                "POST   /api/users/referees/{id}/matches/{matchId}                - Assign referee to match",
                "GET    /api/users/referees/{id}/matches                          - Get assigned matches",
                "PUT    /api/users/referees/{id}/matches/{matchId}/start          - Start match",
                "PUT    /api/users/referees/{id}/matches/{matchId}/result         - Register result",
                "PUT    /api/users/referees/{id}/matches/{matchId}/end            - End match",
                "POST   /api/users/referees/{id}/matches/{matchId}/goals          - Register goal scorer",
                "POST   /api/users/referees/{id}/matches/{matchId}/sanctions      - Register sanction"
        ));
        acciones.put("organizer", List.of(
                "POST   /api/admin/users                                                   - Register organizer (admin only)",
                "GET    /api/users/organizers                                              - List organizers",
                "POST   /api/users/organizers/{id}/tournament                              - Create tournament",
                "PATCH  /api/users/organizers/{id}/tournament/start                        - Start tournament",
                "PATCH  /api/users/organizers/{id}/tournament/end                          - End tournament",
                "PATCH  /api/users/organizers/{id}/tournament/configure                    - Configure tournament",
                "POST   /api/users/organizers/{id}/matches                                 - Create match",
                "GET    /api/users/organizers/{id}/payments/pending                        - View pending payments",
                "PUT    /api/users/organizers/{id}/payments/{paymentId}/approve            - Approve payment",
                "PUT    /api/users/organizers/{id}/payments/{paymentId}/reject             - Reject payment"
        ));

        String actorNormalizado = actor.toLowerCase();
        if (!acciones.containsKey(actorNormalizado)) {
            Map<String, Object> error = new LinkedHashMap<>();
            error.put("error", "Actor '" + actor + "' not valid");
            error.put("availableActors", List.of("player", "captain", "referee", "organizer"));
            return error;
        }

        Map<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put("actor", actorNormalizado);
        respuesta.put("actions", acciones.get(actorNormalizado));
        return respuesta;
    }



    @Operation(summary = "Create player", description = "Registers a new Player in the system with their sports profile (position, jersey number).")
    @PostMapping("/players")
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
    @GetMapping("/players")
    public List<Jugador> listarJugadores() {
        return jugadorService.getJugadores();
    }

    @Operation(summary = "Create sports profile", description = "The Player creates their sports profile with positions, jersey number, age, gender and ID.")
    @PostMapping("/players/{id}/profile")
    public PerfilDeportivo crearPerfilDeportivo(@PathVariable Long id,
                                                @RequestBody Map<String, Object> body) {
        List<Jugador.Posicion> posiciones = ((List<?>) body.get("posiciones")).stream()
                .map(p -> Jugador.Posicion.valueOf(p.toString()))
                .toList();
        int dorsal = Integer.parseInt(body.get("dorsal").toString());
        String foto = body.getOrDefault("foto", "").toString();
        int edad = Integer.parseInt(body.get("edad").toString());
        PerfilDeportivo.Genero genero = PerfilDeportivo.Genero.valueOf(body.get("genero").toString());
        String identificacion = body.get("identificacion").toString();
        Integer semestre = body.containsKey("semestre") ? Integer.parseInt(body.get("semestre").toString()) : null;
        return perfilDeportivoService.crearPerfil(id, posiciones, dorsal, foto, edad, genero, identificacion, semestre);
    }

    @Operation(summary = "Get sports profile", description = "Returns the sports profile of a player.")
    @GetMapping("/players/{id}/profile")
    public PerfilDeportivo consultarPerfilDeportivo(@PathVariable Long id) {
        return perfilDeportivoService.consultarPerfil(id);
    }

    @Operation(summary = "Edit sports profile", description = "The Player updates their position, jersey number, name or photo.")
    @PatchMapping("/players/{id}/profile")
    public Jugador editarPerfil(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        String nombre = body.getOrDefault("nombre", "").toString();
        int numeroCamiseta = body.containsKey("numeroCamiseta") ? Integer.parseInt(body.get("numeroCamiseta").toString()) : 0;
        Jugador.Posicion posicion = body.containsKey("posicion") ? Jugador.Posicion.valueOf(body.get("posicion").toString()) : null;
        String foto = body.getOrDefault("foto", "").toString();
        return jugadorService.editarPerfil(id, nombre, numeroCamiseta, posicion, foto);
    }

    @Operation(summary = "Accept invitation", description = "The Player accepts the invitation sent by a Captain to join their team.")
    @PatchMapping("/players/{id}/accept-invitation")
    public String aceptarInvitacion(@PathVariable Long id) {
        jugadorService.aceptarInvitacion(id);
        return "Invitacion aceptada correctamente";
    }

    @Operation(summary = "Reject invitation", description = "The Player rejects the invitation from a Captain.")
    @PatchMapping("/players/{id}/reject-invitation")
    public String rechazarInvitacion(@PathVariable Long id) {
        jugadorService.rechazarInvitacion(id);
        return "Invitacion rechazada correctamente";
    }

    @Operation(summary = "Mark availability", description = "The Player indicates they are available to be recruited by a team.")
    @PatchMapping("/players/{id}/availability")
    public String marcarDisponible(@PathVariable Long id) {
        jugadorService.marcarDisponible(id);
        return "Jugador marcado como disponible";
    }

    @PostMapping("/players/{id}/photo")
    public String subirFotoJugador(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        return jugadorService.subirFoto(id, file);
    }


    @Operation(summary = "Create captain", description = "Registers a new Captain. The Captain is a Player with permissions to manage a team.")
    @PostMapping("/captains")
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

    @Operation(summary = "List captains", description = "Returns all captains registered in the system.")
    @GetMapping("/captains")
    public List<Capitan> listarCapitanes() {
        return capitanService.getCapitanes();
    }

    @Operation(summary = "Create team", description = "The Captain creates their team to register in the tournament.")
    @PostMapping("/captains/{id}/team")
    public String crearEquipo(@PathVariable Long id, @RequestParam String nombreEquipo) {
        capitanService.crearEquipo(id, nombreEquipo);
        return "Equipo creado correctamente";
    }

    @Operation(summary = "Validate team composition", description = "The Captain verifies if their team meets the rules: minimum 7 and maximum 12 players.")
    @GetMapping("/captains/{id}/team/validate")
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

    @Operation(summary = "Invite player", description = "The Captain sends an invitation to an available Player to join their team.")
    @PostMapping("/captains/{id}/invite/{playerId}")
    public String invitarJugador(@PathVariable Long id, @PathVariable Long playerId) {
        capitanService.invitarJugador(id, playerId);
        return "Invitacion enviada correctamente";
    }

    @Operation(summary = "Define lineup", description = "The Captain defines the starting players for the next match.")
    @PostMapping("/captains/{id}/lineup")
    public String definirAlineacion(@PathVariable Long id, @RequestBody List<Jugador> titulares) {
        return capitanService.definirAlineacion(id, titulares);
    }

    @Operation(summary = "Upload payment receipt", description = "The Captain uploads the registration payment receipt for their team.")
    @PostMapping("/captains/{id}/receipt")
    public String subirComprobante(@PathVariable Long id, @RequestParam String comprobante) {
        return capitanService.subirComprobantePago(id, comprobante);
    }

    @Operation(summary = "Search players by position", description = "The Captain searches for available players filtering by position (PORTERO, DEFENSA, MEDIOCAMPISTA, DELANTERO).")
    @GetMapping("/captains/{id}/search-players")
    public List<Jugador> buscarJugadores(@PathVariable Long id, @RequestParam String posicion) {
        return capitanService.buscarJugadores(posicion);
    }


    @Operation(summary = "Create referee", description = "Registers a new Referee in the system.")
    @PostMapping("/referees")
    public Arbitro crearArbitro(@RequestBody Map<String, Object> body) {
        Arbitro arbitro = new Arbitro(
                null,
                body.get("nombre").toString(),
                body.get("email").toString(),
                body.get("password").toString(),
                Usuario.TipoUsuario.valueOf(body.get("tipoUsuario").toString())
        );
        return arbitroService.save(arbitro);
    }

    @Operation(summary = "List referees", description = "Returns all available referees in the system.")
    @GetMapping("/referees")
    public List<Arbitro> listarArbitros() {
        return arbitroService.getArbitros();
    }

    @Operation(summary = "Assign referee to match", description = "The Organizer assigns a Referee to a scheduled match.")
    @PostMapping("/referees/{id}/matches/{matchId}")
    public String asignarPartido(@PathVariable Long id, @PathVariable Long matchId) {
        Arbitro arbitro = arbitroService.getArbitros().stream()
                .filter(a -> a.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Arbitro no encontrado"));
        Partido partido = partidoRepository.findById(matchId)
                .orElseThrow(() -> new IllegalArgumentException("Partido no encontrado"));
        arbitro.getAssignedMatches().add(partido);
        arbitroService.save(arbitro);
        return "Arbitro asignado al partido correctamente";
    }

    @Operation(summary = "Get referee matches", description = "The Referee queries the matches they have been assigned to referee.")
    @GetMapping("/referees/{id}/matches")
    public List<Partido> consultarPartidosAsignados(@PathVariable Long id) {
        return arbitroService.consultarPartidosAsignados(id);
    }

    @Operation(summary = "Start match", description = "The Referee changes the match status to EN_CURSO.")
    @PutMapping("/referees/{id}/matches/{matchId}/start")
    public Partido iniciarPartido(@PathVariable Long id, @PathVariable Long matchId) {
        return partidoService.iniciarPartido(matchId);
    }

    @Operation(summary = "Register result", description = "The Referee registers the match score.")
    @PutMapping("/referees/{id}/matches/{matchId}/result")
    public Partido registrarResultado(@PathVariable Long id, @PathVariable Long matchId,
                                      @RequestBody Map<String, Integer> body) {
        partidoValidator.validarResultado(body.get("golesLocal"), body.get("golesVisitante"));
        return partidoService.registrarResultado(matchId, body.get("golesLocal"), body.get("golesVisitante"));
    }

    @Operation(summary = "End match", description = "The Referee closes the match and marks it as FINALIZADO.")
    @PutMapping("/referees/{id}/matches/{matchId}/end")
    public Partido finalizarPartido(@PathVariable Long id, @PathVariable Long matchId) {
        return partidoService.finalizarPartido(matchId);
    }

    @Operation(summary = "Register goal scorer", description = "The Referee registers a goal indicating the player and the minute.")
    @PostMapping("/referees/{id}/matches/{matchId}/goals")
    public Partido registrarGoleador(@PathVariable Long id, @PathVariable Long matchId,
                                     @RequestBody Map<String, Object> body) {
        Long jugadorId = Long.valueOf(body.get("jugadorId").toString());
        int minuto = Integer.parseInt(body.get("minuto").toString());
        partidoValidator.validarGoleador(jugadorId, minuto);
        return partidoService.registrarGoleador(matchId, jugadorId, minuto);
    }

    @Operation(summary = "Register sanction", description = "Registers a sanction for a player with type and description.")
    @PostMapping("/referees/{id}/matches/{matchId}/sanctions")
    public Partido registrarSancion(@PathVariable Long id, @PathVariable Long matchId,
                                    @RequestBody Map<String, Object> body) {
        Long jugadorId = Long.valueOf(body.get("jugadorId").toString());
        Sancion.TipoSancion tipoSancion = Sancion.TipoSancion.valueOf(body.get("tipoSancion").toString());
        String descripcion = body.get("descripcion").toString();
        Jugador jugador = jugadorRepository.findById(jugadorId)
                .orElseThrow(() -> new IllegalArgumentException("Jugador no encontrado"));
        Sancion sancion = new Sancion();
        sancion.setJugador(jugador);
        sancion.setTipoSancion(tipoSancion);
        sancion.setDescripcion(descripcion);
        partidoValidator.validarSancion(sancion);
        return partidoService.registrarSancion(matchId, jugadorId, tipoSancion, descripcion);
    }


    @Operation(summary = "Create organizer", description = "Registers a new Organizer in the system.")
    @PostMapping("/organizers")
    public Organizador crearOrganizador(@RequestBody Map<String, Object> body) {
        Organizador organizador = new Organizador(
                null,
                body.get("nombre").toString(),
                body.get("email").toString(),
                body.get("password").toString(),
                Usuario.TipoUsuario.valueOf(body.get("tipoUsuario").toString()),
                null
        );
        return organizadorService.save(organizador);
    }

    @Operation(summary = "List organizers", description = "Returns all organizers registered in the system.")
    @GetMapping("/organizers")
    public List<Organizador> listarOrganizadores() {
        return organizadorService.getOrganizadores();
    }

    @Operation(summary = "Create tournament", description = "The Organizer creates a new tournament with name, dates, number of teams and registration cost.")
    @PostMapping("/organizers/{id}/tournament")
    public Torneo crearTorneo(@PathVariable Long id, @RequestBody Torneo torneo) {
        return organizadorService.crearTorneo(id, torneo);
    }

    @Operation(summary = "Start tournament", description = "The Organizer changes the tournament status to EN_CURSO, enabling match registration.")
    @PatchMapping("/organizers/{id}/tournament/start")
    public Torneo iniciarTorneo(@PathVariable Long id) {
        return organizadorService.iniciarTorneo(id);
    }

    @Operation(summary = "End tournament", description = "The Organizer closes the tournament and marks it as FINALIZADO.")
    @PatchMapping("/organizers/{id}/tournament/end")
    public Torneo finalizarTorneo(@PathVariable Long id) {
        return organizadorService.finalizarTorneo(id);
    }

    @Operation(summary = "Configure tournament", description = "The Organizer defines the regulations, courts, schedules, sanctions and registration closing date.")
    @PatchMapping("/organizers/{id}/tournament/configure")
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

    @Operation(summary = "Create match", description = "The Organizer schedules a match between two teams within a tournament.")
    @PostMapping("/organizers/{id}/matches")
    public Partido crearPartido(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        Long torneoId = Long.valueOf(body.get("torneoId").toString());
        Long equipoLocalId = Long.valueOf(body.get("equipoLocalId").toString());
        Long equipoVisitanteId = Long.valueOf(body.get("equipoVisitanteId").toString());
        LocalDateTime fecha = LocalDateTime.parse(body.get("fecha").toString());
        String cancha = body.get("cancha").toString();
        partidoValidator.validarCrearPartido(torneoId, equipoLocalId, equipoVisitanteId, fecha, cancha);
        return partidoService.crearPartido(torneoId, equipoLocalId, equipoVisitanteId, fecha, cancha);
    }

    @Operation(summary = "View pending payments", description = "The Organizer queries all payment receipts that have not yet been verified.")
    @GetMapping("/organizers/{id}/payments/pending")
    public List<Pago> pagosPendientes(@PathVariable Long id) {
        return pagoService.consultarPagosPendientes();
    }

    @Operation(summary = "Approve payment", description = "The Organizer approves a team's payment receipt, confirming their registration.")
    @PutMapping("/organizers/{id}/payments/{paymentId}/approve")
    public Pago aprobarPago(@PathVariable Long id, @PathVariable Long paymentId) {
        return pagoService.aprobarPago(paymentId);
    }

    @Operation(summary = "Reject payment", description = "The Organizer rejects a team's payment receipt for being invalid or incomplete.")
    @PutMapping("/organizers/{id}/payments/{paymentId}/reject")
    public Pago rechazarPago(@PathVariable Long id, @PathVariable Long paymentId) {
        return pagoService.rechazarPago(paymentId);
    }
}