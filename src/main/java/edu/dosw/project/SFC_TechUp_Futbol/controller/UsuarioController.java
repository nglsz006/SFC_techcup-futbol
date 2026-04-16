package edu.dosw.project.SFC_TechUp_Futbol.controller;

import edu.dosw.project.SFC_TechUp_Futbol.controller.dto.response.ArbitroResponse;
import edu.dosw.project.SFC_TechUp_Futbol.controller.dto.response.CapitanResponse;
import edu.dosw.project.SFC_TechUp_Futbol.controller.dto.response.JugadorResponse;
import edu.dosw.project.SFC_TechUp_Futbol.controller.dto.response.OrganizadorResponse;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.*;
import edu.dosw.project.SFC_TechUp_Futbol.core.service.*;
import edu.dosw.project.SFC_TechUp_Futbol.core.util.IdGeneratorUtil;
import edu.dosw.project.SFC_TechUp_Futbol.core.validator.PartidoValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.logging.Logger;

@Tag(name = "Users", description = "Management of system actors: Players, Captains, Referees and Organizers.")
@RestController
@RequestMapping("/api/users")
public class UsuarioController {

    private static final Logger log = Logger.getLogger(UsuarioController.class.getName());

    private static String sanitize(String input) {
        return input == null ? "null" : input.replaceAll("[\r\n\t]", "_");
    }

    private final JugadorService jugadorService;
    private final CapitanService capitanService;
    private final ArbitroService arbitroService;
    private final OrganizadorService organizadorService;
    private final PagoService pagoService;
    private final PartidoService partidoService;
    private final PartidoValidator partidoValidator;
    private final EquipoService equipoService;
    private final TorneoService torneoService;
    private final PerfilDeportivoService perfilDeportivoService;

    public UsuarioController(JugadorService jugadorService,
                             CapitanService capitanService, ArbitroService arbitroService,
                             OrganizadorService organizadorService,
                             PagoService pagoService, PartidoService partidoService,
                             PartidoValidator partidoValidator, EquipoService equipoService,
                             TorneoService torneoService, PerfilDeportivoService perfilDeportivoService) {
        this.jugadorService = jugadorService;
        this.capitanService = capitanService;
        this.arbitroService = arbitroService;
        this.organizadorService = organizadorService;
        this.pagoService = pagoService;
        this.partidoService = partidoService;
        this.partidoValidator = partidoValidator;
        this.equipoService = equipoService;
        this.torneoService = torneoService;
        this.perfilDeportivoService = perfilDeportivoService;
    }

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get actions by actor")
    @GetMapping("/{actor}")
    public Map<String, Object> accionesPorActor(@PathVariable String actor) {
        Map<String, List<String>> acciones = new LinkedHashMap<>();
        acciones.put("player", List.of("POST /api/users/players", "GET /api/users/players"));
        acciones.put("captain", List.of("POST /api/users/captains", "GET /api/users/captains"));
        acciones.put("referee", List.of("POST /api/users/referees", "GET /api/users/referees"));
        acciones.put("organizer", List.of("POST /api/users/organizers", "GET /api/users/organizers"));
        String actorNormalizado = actor.toLowerCase(Locale.ROOT);
        if (!acciones.containsKey(actorNormalizado)) {
            Map<String, Object> error = new LinkedHashMap<>();
            error.put("error", "Actor not valid");
            error.put("availableActors", List.of("player", "captain", "referee", "organizer"));
            log.warning("Actor inválido recibido: " + sanitize(actor));
            return error;
        }
        Map<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put("actor", actorNormalizado);
        respuesta.put("actions", acciones.get(actorNormalizado));
        return respuesta;
    }

//jugadores

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Create player")
    @PostMapping("/players")
    public Map<String, String> crearJugador(@RequestBody Map<String, Object> body) {
        Jugador jugador = new Jugador(
                null,
                body.get("nombre").toString(),
                body.get("email").toString(),
                body.get("password").toString(),
                Usuario.TipoUsuario.valueOf(body.get("tipoUsuario").toString()),
                Integer.parseInt(body.get("numeroCamiseta").toString()),
                Jugador.Posicion.valueOf(body.get("posicion").toString()),
                true,
                ""
        );
        jugadorService.save(jugador);
        return Map.of("mensaje", "Jugador registrado correctamente.");
    }

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "List players")
    @GetMapping("/players")
    public List<JugadorResponse> listarJugadores() {
        return jugadorService.getJugadores().stream().map(JugadorResponse::new).toList();
    }

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Create sports profile")
    @PostMapping("/players/{id}/profile")
    public PerfilDeportivo crearPerfilDeportivo(@PathVariable String id, @RequestBody Map<String, Object> body) {
        List<Jugador.Posicion> posiciones = ((List<?>) body.get("posiciones")).stream()
                .map(p -> Jugador.Posicion.valueOf(p.toString())).toList();
        int dorsal = Integer.parseInt(body.get("dorsal").toString());
        String foto = body.getOrDefault("foto", "").toString();
        int edad = Integer.parseInt(body.get("edad").toString());
        PerfilDeportivo.Genero genero = PerfilDeportivo.Genero.valueOf(body.get("genero").toString());
        String identificacion = body.get("identificacion").toString();
        Integer semestre = body.containsKey("semestre") ? Integer.parseInt(body.get("semestre").toString()) : null;
        return perfilDeportivoService.crearPerfil(id, posiciones, dorsal, foto, edad, genero, identificacion, semestre);
    }

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get sports profile")
    @GetMapping("/players/{id}/profile")
    public PerfilDeportivo consultarPerfilDeportivo(@PathVariable String id) {
        return perfilDeportivoService.consultarPerfil(id);
    }

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Edit sports profile")
    @PatchMapping("/players/{id}/profile")
    public JugadorResponse editarPerfil(@PathVariable String id, @RequestBody Map<String, Object> body) {
        String nombre = body.getOrDefault("nombre", "").toString();
        int numeroCamiseta = body.containsKey("numeroCamiseta") ? Integer.parseInt(body.get("numeroCamiseta").toString()) : 0;
        Jugador.Posicion posicion = body.containsKey("posicion") ? Jugador.Posicion.valueOf(body.get("posicion").toString()) : null;
        String foto = body.getOrDefault("foto", "").toString();
        return new JugadorResponse(jugadorService.editarPerfil(id, nombre, numeroCamiseta, posicion, foto));
    }

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Accept invitation")
    @PatchMapping("/players/{id}/accept-invitation")
    public String aceptarInvitacion(@PathVariable String id) {
        jugadorService.aceptarInvitacion(id);
        return "Invitacion aceptada correctamente";
    }

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Reject invitation")
    @PatchMapping("/players/{id}/reject-invitation")
    public String rechazarInvitacion(@PathVariable String id) {
        jugadorService.rechazarInvitacion(id);
        return "Invitacion rechazada correctamente";
    }

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Mark availability")
    @PatchMapping("/players/{id}/availability")
    public String marcarDisponible(@PathVariable String id) {
        jugadorService.marcarDisponible(id);
        return "Jugador marcado como disponible";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/players/{id}/photo")
    public String subirFotoJugador(@PathVariable String id, @RequestParam("file") MultipartFile file) {
        return jugadorService.subirFoto(id, file);
    }

    @PreAuthorize("hasAnyRole('ORGANIZADOR', 'ADMINISTRADOR')")
    @Operation(
        summary = "Delete player",
        description = "Deletes a player by ID. Only ORGANIZADOR or ADMINISTRADOR can perform this action."
    )
    @DeleteMapping("/players/{id}")
    public Map<String, String> eliminarJugador(@PathVariable String id) {
        jugadorService.eliminar(id);
        return Map.of("mensaje", "Jugador eliminado correctamente.");
    }

// capitanes

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Create captain")
    @PostMapping("/captains")
    public CapitanResponse crearCapitan(@RequestBody Map<String, Object> body) {
        Capitan capitan = new Capitan(
                null,
                body.get("nombre").toString(),
                body.get("email").toString(),
                body.get("password").toString(),
                Usuario.TipoUsuario.valueOf(body.get("tipoUsuario").toString()),
                Integer.parseInt(body.get("numeroCamiseta").toString()),
                Jugador.Posicion.valueOf(body.get("posicion").toString()),
                true,
                "",
                null
        );
        Capitan guardado = capitanService.save(capitan);
        return new CapitanResponse(guardado);
    }

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "List captains")
    @GetMapping("/captains")
    public List<CapitanResponse> listarCapitanes() {
        return capitanService.getCapitanes().stream().map(CapitanResponse::new).toList();
    }

    @PreAuthorize("hasRole('CAPITAN')")
    @Operation(summary = "Create team")
    @PostMapping("/captains/{id}/team")
    public String crearEquipo(@PathVariable String id, @RequestParam String nombreEquipo) {
        capitanService.crearEquipo(id, nombreEquipo);
        return "Equipo creado correctamente";
    }

    @PreAuthorize("hasRole('CAPITAN')")
    @Operation(summary = "Validate team composition")
    @GetMapping("/captains/{id}/team/validate")
    public Map<String, Object> validarEquipo(@PathVariable String id) {
        Capitan capitan = capitanService.getCapitanes().stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Capitán no encontrado"));
        Equipo equipo = equipoService.listar().stream()
                .filter(e -> e.getCapitanId() != null && e.getCapitanId().equals(capitan.getId()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("El capitán no tiene equipo registrado"));
        return equipoService.validarComposicion(equipo.getId());
    }

    @PreAuthorize("hasRole('CAPITAN')")
    @Operation(summary = "Invite player")
    @PostMapping("/captains/{id}/invite/{playerId}")
    public String invitarJugador(@PathVariable String id, @PathVariable String playerId) {
        capitanService.invitarJugador(id, playerId);
        return "Invitacion enviada correctamente";
    }

    @PreAuthorize("hasRole('CAPITAN')")
    @Operation(summary = "Define lineup")
    @PostMapping("/captains/{id}/lineup")
    public String definirAlineacion(@PathVariable String id, @RequestBody List<Jugador> titulares) {
        return capitanService.definirAlineacion(id, titulares);
    }

    @PreAuthorize("hasRole('CAPITAN')")
    @Operation(summary = "Upload payment receipt")
    @PostMapping("/captains/{id}/receipt")
    public String subirComprobante(@PathVariable String id, @RequestParam String comprobante) {
        return capitanService.subirComprobantePago(id, comprobante);
    }

    @PreAuthorize("hasRole('CAPITAN')")
    @Operation(summary = "Search players by filters")
    @GetMapping("/captains/{id}/search-players/advanced")
    public List<PerfilDeportivo> buscarJugadoresAvanzado(
            @PathVariable String id,
            @RequestParam(required = false) String posicion,
            @RequestParam(required = false) Integer semestre,
            @RequestParam(required = false) Integer edad,
            @RequestParam(required = false) String genero,
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String identificacion) {
        Jugador.Posicion pos = posicion != null ? Jugador.Posicion.valueOf(posicion) : null;
        PerfilDeportivo.Genero gen = genero != null ? PerfilDeportivo.Genero.valueOf(genero) : null;
        return perfilDeportivoService.buscarJugadores(pos, semestre, edad, gen, nombre, identificacion);
    }

    @PreAuthorize("hasRole('CAPITAN')")
    @Operation(summary = "Search players by position")
    @GetMapping("/captains/{id}/search-players")
    public List<JugadorResponse> buscarJugadores(@PathVariable String id, @RequestParam String posicion) {
        return capitanService.buscarJugadores(posicion).stream().map(JugadorResponse::new).toList();
    }

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Toggle role between player and captain")
    @PatchMapping("/players/{id}/profile/toggle-role")
    public Map<String, String> toggleRol(@PathVariable String id) {
        return Map.of("mensaje", capitanService.toggleRol(id));
    }

//arbitros

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Create referee")
    @PostMapping("/referees")
    public Map<String, String> crearArbitro(@RequestBody Map<String, Object> body) {
        Arbitro arbitro = new Arbitro(
                null,
                body.get("nombre").toString(),
                body.get("email").toString(),
                body.get("password").toString(),
                Usuario.TipoUsuario.valueOf(body.get("tipoUsuario").toString())
        );
        arbitroService.save(arbitro);
        return Map.of("mensaje", "Árbitro registrado correctamente.");
    }

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "List referees")
    @GetMapping("/referees")
    public List<ArbitroResponse> listarArbitros() {
        return arbitroService.getArbitros().stream().map(ArbitroResponse::new).toList();
    }

    @PreAuthorize("hasRole('ARBITRO')")
    @Operation(summary = "Assign referee to match")
    @PostMapping("/referees/{id}/matches/{matchId}")
    public String asignarPartido(@PathVariable String id, @PathVariable String matchId) {
        Arbitro arbitro = arbitroService.getArbitros().stream()
                .filter(a -> a.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Arbitro no encontrado"));
        Partido partido = partidoService.consultarPartido(matchId);
        arbitro.getAssignedMatches().add(partido);
        arbitroService.save(arbitro);
        return "Arbitro asignado al partido correctamente";
    }

    @PreAuthorize("hasRole('ARBITRO')")
    @Operation(summary = "Get assigned pending matches")
    @GetMapping("/referees/{id}/matches/pending")
    public List<Partido> consultarPartidosPendientes(@PathVariable String id) {
        return arbitroService.consultarPartidosPendientes(id);
    }

    @PreAuthorize("hasRole('ARBITRO')")
    @Operation(summary = "Get refereed (finished) matches")
    @GetMapping("/referees/{id}/matches/history")
    public List<Partido> consultarPartidosArbitrados(@PathVariable String id) {
        return arbitroService.consultarPartidosArbitrados(id);
    }

    @PreAuthorize("hasRole('ARBITRO')")
    @Operation(summary = "Get match detail with action flags")
    @GetMapping("/referees/{id}/matches/{matchId}")
    public Map<String, Object> detallePartido(@PathVariable String id, @PathVariable String matchId) {
        if (!arbitroService.tienePartido(id, matchId))
            throw new IllegalArgumentException("El partido no pertenece a este arbitro");
        Partido partido = partidoService.consultarPartido(matchId);
        Map<String, Object> resp = new LinkedHashMap<>();
        resp.put("partido", partido);
        resp.put("canStart", partido.getEstado() == Partido.PartidoEstado.PROGRAMADO);
        resp.put("canFinish", partido.getEstado() == Partido.PartidoEstado.EN_CURSO);
        resp.put("canRegisterEvents", partido.getEstado() == Partido.PartidoEstado.EN_CURSO);
        return resp;
    }

    @PreAuthorize("hasRole('ARBITRO')")
    @Operation(summary = "Get match lineups")
    @GetMapping("/referees/{id}/matches/{matchId}/lineups")
    public Map<String, Object> consultarAlineaciones(@PathVariable String id, @PathVariable String matchId) {
        if (!arbitroService.tienePartido(id, matchId))
            throw new IllegalArgumentException("El partido no pertenece a este arbitro");
        return partidoService.consultarEventos(matchId);
    }

    @PreAuthorize("hasRole('ARBITRO')")
    @Operation(summary = "Register card")
    @PostMapping("/referees/{id}/matches/{matchId}/cards")
    public Partido registrarTarjeta(@PathVariable String id, @PathVariable String matchId,
                                    @RequestBody Map<String, Object> body) {
        if (!arbitroService.tienePartido(id, matchId))
            throw new IllegalArgumentException("El partido no pertenece a este arbitro");
        String jugadorId = body.get("jugadorId").toString();
        Partido.Tarjeta.TipoTarjeta tipo = Partido.Tarjeta.TipoTarjeta.valueOf(body.get("tipo").toString());
        int minuto = Integer.parseInt(body.get("minuto").toString());
        return partidoService.registrarTarjeta(matchId, jugadorId, tipo, minuto);
    }

    @PreAuthorize("hasRole('ARBITRO')")
    @Operation(summary = "Get referee matches")
    @GetMapping("/referees/{id}/matches")
    public List<Partido> consultarPartidosAsignados(@PathVariable String id) {
        return arbitroService.consultarPartidosAsignados(id);
    }

    @PreAuthorize("hasRole('ARBITRO')")
    @Operation(summary = "Start match")
    @PutMapping("/referees/{id}/matches/{matchId}/start")
    public Partido iniciarPartido(@PathVariable String id, @PathVariable String matchId) {
        return partidoService.iniciarPartido(matchId);
    }

    @PreAuthorize("hasRole('ARBITRO')")
    @Operation(summary = "Register result")
    @PutMapping("/referees/{id}/matches/{matchId}/result")
    public Partido registrarResultado(@PathVariable String id, @PathVariable String matchId,
                                      @RequestBody Map<String, Integer> body) {
        partidoValidator.validarResultado(body.get("golesLocal"), body.get("golesVisitante"));
        return partidoService.registrarResultado(matchId, body.get("golesLocal"), body.get("golesVisitante"));
    }

    @PreAuthorize("hasRole('ARBITRO')")
    @Operation(summary = "End match")
    @PutMapping("/referees/{id}/matches/{matchId}/end")
    public Partido finalizarPartido(@PathVariable String id, @PathVariable String matchId) {
        return partidoService.finalizarPartido(matchId);
    }

    @PreAuthorize("hasRole('ARBITRO')")
    @Operation(summary = "Register goal scorer")
    @PostMapping("/referees/{id}/matches/{matchId}/goals")
    public Partido registrarGoleador(@PathVariable String id, @PathVariable String matchId,
                                     @RequestBody Map<String, Object> body) {
        String jugadorId = body.get("jugadorId").toString();
        int minuto = Integer.parseInt(body.get("minuto").toString());
        return partidoService.registrarGoleador(matchId, jugadorId, minuto);
    }

    @PreAuthorize("hasRole('ARBITRO')")
    @Operation(summary = "Register sanction")
    @PostMapping("/referees/{id}/matches/{matchId}/sanctions")
    public Partido registrarSancion(@PathVariable String id, @PathVariable String matchId,
                                    @RequestBody Map<String, Object> body) {
        String jugadorId = body.get("jugadorId").toString();
        Sancion.TipoSancion tipoSancion = Sancion.TipoSancion.valueOf(body.get("tipoSancion").toString());
        String descripcion = sanitize(body.get("descripcion").toString());
        Jugador jugador = jugadorService.buscarJugadorPorId(jugadorId);
        if (jugador == null) throw new IllegalArgumentException("Jugador no encontrado");
        Sancion sancion = new Sancion();
        sancion.setId(IdGeneratorUtil.generarId());
        sancion.setJugador(jugador);
        sancion.setTipoSancion(tipoSancion);
        sancion.setDescripcion(descripcion);
        partidoValidator.validarSancion(sancion);
        return partidoService.registrarSancion(matchId, jugadorId, tipoSancion, descripcion);
    }

// organizadores

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Create organizer")
    @PostMapping("/organizers")
    public OrganizadorResponse crearOrganizador(@RequestBody Map<String, Object> body) {
        Organizador organizador = new Organizador(
                null,
                body.get("nombre").toString(),
                body.get("email").toString(),
                body.get("password").toString(),
                Usuario.TipoUsuario.valueOf(body.get("tipoUsuario").toString()),
                null
        );
        Organizador guardado = organizadorService.save(organizador);
        return new OrganizadorResponse(guardado);
    }

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "List organizers")
    @GetMapping("/organizers")
    public List<OrganizadorResponse> listarOrganizadores() {
        return organizadorService.getOrganizadores().stream().map(OrganizadorResponse::new).toList();
    }

    @PreAuthorize("hasRole('ORGANIZADOR')")
    @Operation(summary = "Create tournament")
    @PostMapping("/organizers/{id}/tournament")
    public Torneo crearTorneo(@PathVariable String id, @RequestBody Torneo torneo) {
        return organizadorService.crearTorneo(id, torneo);
    }

    @PreAuthorize("hasRole('ORGANIZADOR')")
    @Operation(summary = "Start tournament")
    @PatchMapping("/organizers/{id}/tournament/start")
    public Torneo iniciarTorneo(@PathVariable String id) {
        return organizadorService.iniciarTorneo(id);
    }

    @PreAuthorize("hasRole('ORGANIZADOR')")
    @Operation(summary = "End tournament")
    @PatchMapping("/organizers/{id}/tournament/end")
    public Torneo finalizarTorneo(@PathVariable String id) {
        return organizadorService.finalizarTorneo(id);
    }

    @PreAuthorize("hasRole('ORGANIZADOR')")
    @Operation(summary = "Configure tournament")
    @PatchMapping("/organizers/{id}/tournament/configure")
    public Torneo configurarTorneo(@PathVariable String id, @RequestBody Map<String, Object> body) {
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

    @PreAuthorize("hasRole('ORGANIZADOR')")
    @Operation(summary = "Register match result")
    @PutMapping("/organizers/{id}/matches/{matchId}/result")
    public Partido registrarResultadoOrganizador(@PathVariable String id, @PathVariable String matchId,
                                      @RequestBody Map<String, Integer> body) {
        partidoValidator.validarResultado(body.get("golesLocal"), body.get("golesVisitante"));
        return partidoService.registrarResultado(matchId, body.get("golesLocal"), body.get("golesVisitante"));
    }

    @PreAuthorize("hasRole('ORGANIZADOR')")
    @Operation(summary = "Register goal scorer from organizer")
    @PostMapping("/organizers/{id}/matches/{matchId}/goals")
    public Partido registrarGoleadorOrganizador(@PathVariable String id, @PathVariable String matchId,
                                                @RequestBody Map<String, Object> body) {
        String jugadorId = body.get("jugadorId").toString();
        int minuto = Integer.parseInt(body.get("minuto").toString());
        return partidoService.registrarGoleador(matchId, jugadorId, minuto);
    }

    @PreAuthorize("hasRole('ORGANIZADOR')")
    @Operation(summary = "Register sanction from organizer")
    @PostMapping("/organizers/{id}/matches/{matchId}/sanctions")
    public Partido registrarSancionOrganizador(@PathVariable String id, @PathVariable String matchId,
                                               @RequestBody Map<String, Object> body) {
        String jugadorId = body.get("jugadorId").toString();
        Sancion.TipoSancion tipoSancion = Sancion.TipoSancion.valueOf(body.get("tipoSancion").toString());
        String descripcion = sanitize(body.get("descripcion").toString());
        return partidoService.registrarSancion(matchId, jugadorId, tipoSancion, descripcion);
    }

    @PreAuthorize("hasRole('ORGANIZADOR')")
    @Operation(summary = "Create match")
    @PostMapping("/organizers/{id}/matches")
    public Partido crearPartido(@PathVariable String id, @RequestBody Map<String, Object> body) {
        String torneoId = body.get("torneoId").toString();
        String equipoLocalId = body.get("equipoLocalId").toString();
        String equipoVisitanteId = body.get("equipoVisitanteId").toString();
        LocalDateTime fecha = LocalDateTime.parse(body.get("fecha").toString());
        String cancha = body.get("cancha").toString();
        return partidoService.crearPartido(torneoId, equipoLocalId, equipoVisitanteId, fecha, cancha);
    }

    @PreAuthorize("hasRole('ORGANIZADOR')")
    @Operation(summary = "View pending payments")
    @GetMapping("/organizers/{id}/payments/pending")
    public List<Pago> pagosPendientes(@PathVariable String id) {
        return pagoService.consultarPagosPendientes();
    }

    @PreAuthorize("hasRole('ORGANIZADOR')")
    @Operation(summary = "Approve payment")
    @PutMapping("/organizers/{id}/payments/{paymentId}/approve")
    public Pago aprobarPago(@PathVariable String id, @PathVariable String paymentId) {
        return pagoService.aprobarPago(paymentId);
    }

    @PreAuthorize("hasRole('ORGANIZADOR')")
    @Operation(summary = "Reject payment")
    @PutMapping("/organizers/{id}/payments/{paymentId}/reject")
    public Pago rechazarPago(@PathVariable String id, @PathVariable String paymentId) {
        return pagoService.rechazarPago(paymentId);
    }

}
