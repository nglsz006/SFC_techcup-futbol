package edu.dosw.project.SFC_TechUp_Futbol.controller;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.*;
import edu.dosw.project.SFC_TechUp_Futbol.core.repository.JugadorRepository;
import edu.dosw.project.SFC_TechUp_Futbol.core.repository.PartidoRepository;
import edu.dosw.project.SFC_TechUp_Futbol.core.service.*;
import edu.dosw.project.SFC_TechUp_Futbol.core.validator.PartidoValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;

@Tag(name = "Usuarios", description = "Gestión de los actores del sistema: Jugadores, Capitanes, Árbitros y Organizadores.")
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

    @Operation(summary = "Consultar acciones por actor", description = "Retorna las acciones disponibles para un actor del sistema. Actores válidos: jugador, capitan, arbitro, organizador.")
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
            "POST   /api/usuarios/arbitros                                    - Crear arbitro",
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
            "POST   /api/usuarios/organizadores                                        - Crear organizador",
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

    @Operation(summary = "Crear jugador", description = "Registra un nuevo Jugador en el sistema con su perfil deportivo (posición, número de camiseta).")
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

    @Operation(summary = "Listar jugadores", description = "Retorna todos los jugadores registrados. Útil para el Capitán al buscar integrantes para su equipo.")
    @GetMapping("/jugadores")
    public List<Jugador> listarJugadores() {
        return jugadorService.getJugadores();
    }

    @Operation(summary = "Editar perfil deportivo", description = "El Jugador actualiza su posición, número de camiseta, nombre o foto.")
    @PatchMapping("/jugadores/{id}/perfil")
    public Jugador editarPerfil(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        String nombre = body.getOrDefault("nombre", "").toString();
        int numeroCamiseta = body.containsKey("numeroCamiseta") ? Integer.parseInt(body.get("numeroCamiseta").toString()) : 0;
        Jugador.Posicion posicion = body.containsKey("posicion") ? Jugador.Posicion.valueOf(body.get("posicion").toString()) : null;
        String foto = body.getOrDefault("foto", "").toString();
        return jugadorService.editarPerfil(id, nombre, numeroCamiseta, posicion, foto);
    }

    @Operation(summary = "Aceptar invitación", description = "El Jugador acepta la invitación enviada por un Capitán para unirse a su equipo.")
    @PatchMapping("/jugadores/{id}/aceptarInvitacion")
    public String aceptarInvitacion(@PathVariable Long id) {
        jugadorService.aceptarInvitacion(id);
        return "Invitacion aceptada correctamente";
    }

    @Operation(summary = "Rechazar invitación", description = "El Jugador rechaza la invitación de un Capitán.")
    @PatchMapping("/jugadores/{id}/rechazarInvitacion")
    public String rechazarInvitacion(@PathVariable Long id) {
        jugadorService.rechazarInvitacion(id);
        return "Invitacion rechazada correctamente";
    }

    @Operation(summary = "Marcar disponibilidad", description = "El Jugador indica que está disponible para ser convocado a un equipo.")
    @PatchMapping("/jugadores/{id}/disponibilidad")
    public String marcarDisponible(@PathVariable Long id) {
        jugadorService.marcarDisponible(id);
        return "Jugador marcado como disponible";
    }

    // ── Capitanes ──────────────────────────────────────────────────────────────

    @Operation(summary = "Crear capitán", description = "Registra un nuevo Capitán. El Capitán es un Jugador con permisos para gestionar un equipo.")
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

    @Operation(summary = "Validar composición del equipo", description = "El Capitán verifica si su equipo cumple las reglas: mínimo 7 y máximo 12 jugadores.")
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

    @Operation(summary = "Listar capitanes", description = "Retorna todos los capitanes registrados en el sistema.")
    @GetMapping("/capitanes")
    public List<Capitan> listarCapitanes() {
        return capitanService.getCapitanes();
    }

    @Operation(summary = "Crear equipo", description = "El Capitán crea su equipo para inscribirse en el torneo.")
    @PostMapping("/capitanes/{id}/equipo")
    public String crearEquipo(@PathVariable Long id, @RequestParam String nombreEquipo) {
        capitanService.crearEquipo(id, nombreEquipo);
        return "Equipo creado correctamente";
    }

    @Operation(summary = "Invitar jugador", description = "El Capitán envía una invitación a un Jugador disponible para unirse a su equipo.")
    @PostMapping("/capitanes/{id}/invitar/{jugadorId}")
    public String invitarJugador(@PathVariable Long id, @PathVariable Long jugadorId) {
        capitanService.invitarJugador(id, jugadorId);
        return "Invitacion enviada correctamente";
    }

    @Operation(summary = "Definir alineación", description = "El Capitán define los jugadores titulares para el próximo partido.")
    @PostMapping("/capitanes/{id}/alineacion")
    public String definirAlineacion(@PathVariable Long id, @RequestBody List<Jugador> titulares) {
        return capitanService.definirAlineacion(id, titulares);
    }

    @Operation(summary = "Subir comprobante de pago", description = "El Capitán sube el comprobante de pago de inscripción de su equipo.")
    @PostMapping("/capitanes/{id}/comprobante")
    public String subirComprobante(@PathVariable Long id, @RequestParam String comprobante) {
        return capitanService.subirComprobantePago(id, comprobante);
    }

    @Operation(summary = "Buscar jugadores por posición", description = "El Capitán busca jugadores disponibles filtrando por posición (PORTERO, DEFENSA, MEDIOCAMPISTA, DELANTERO).")
    @GetMapping("/capitanes/{id}/buscarJugadores")
    public List<Jugador> buscarJugadores(@PathVariable Long id, @RequestParam String posicion) {
        return capitanService.buscarJugadores(posicion);
    }

    // ── Arbitros ───────────────────────────────────────────────────────────────

    @Operation(summary = "Crear árbitro", description = "El Organizador registra un nuevo Árbitro en el sistema.")
    @PostMapping("/arbitros")
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

    @Operation(summary = "Listar árbitros", description = "Retorna todos los árbitros disponibles en el sistema.")
    @GetMapping("/arbitros")
    public List<Arbitro> listarArbitros() {
        return arbitroService.getArbitros();
    }

    @Operation(summary = "Asignar árbitro a partido", description = "El Organizador asigna un Árbitro a un partido programado.")
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

    @Operation(summary = "Consultar partidos del árbitro", description = "El Árbitro consulta los partidos que tiene asignados para arbitrar.")
    @GetMapping("/arbitros/{id}/partidos")
    public List<Partido> consultarPartidosAsignados(@PathVariable Long id) {
        return arbitroService.consultarPartidosAsignados(id);
    }

    @Operation(summary = "Iniciar partido", description = "El Árbitro cambia el estado del partido a EN_CURSO.")
    @PutMapping("/arbitros/{id}/partidos/{partidoId}/iniciar")
    public Partido iniciarPartido(@PathVariable Long id, @PathVariable Long partidoId) {
        return partidoService.iniciarPartido(partidoId);
    }

    @Operation(summary = "Registrar resultado", description = "El Árbitro registra el marcador del partido.")
    @PutMapping("/arbitros/{id}/partidos/{partidoId}/resultado")
    public Partido registrarResultado(@PathVariable Long id, @PathVariable Long partidoId,
                                      @RequestBody Map<String, Integer> body) {
        partidoValidator.validarResultado(body.get("golesLocal"), body.get("golesVisitante"));
        return partidoService.registrarResultado(partidoId, body.get("golesLocal"), body.get("golesVisitante"));
    }

    @Operation(summary = "Finalizar partido", description = "El Árbitro cierra el partido y lo marca como FINALIZADO.")
    @PutMapping("/arbitros/{id}/partidos/{partidoId}/finalizar")
    public Partido finalizarPartido(@PathVariable Long id, @PathVariable Long partidoId) {
        return partidoService.finalizarPartido(partidoId);
    }

    @Operation(summary = "Registrar goleador", description = "El Árbitro registra un gol indicando el jugador y el minuto.")
    @PostMapping("/arbitros/{id}/partidos/{partidoId}/goles")
    public Partido registrarGoleador(@PathVariable Long id, @PathVariable Long partidoId,
                                     @RequestBody Map<String, Object> body) {
        Long jugadorId = Long.valueOf(body.get("jugadorId").toString());
        int minuto = Integer.parseInt(body.get("minuto").toString());
        partidoValidator.validarGoleador(jugadorId, minuto);
        return partidoService.registrarGoleador(partidoId, jugadorId, minuto);
    }

    @Operation(summary = "Registrar tarjeta", description = "El Árbitro registra una tarjeta (AMARILLA o ROJA) a un jugador.")
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

    @Operation(summary = "Crear organizador", description = "Registra un nuevo Organizador, responsable de administrar el torneo.")
    @PostMapping("/organizadores")
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

    @Operation(summary = "Listar organizadores", description = "Retorna todos los organizadores registrados en el sistema.")
    @GetMapping("/organizadores")
    public List<Organizador> listarOrganizadores() {
        return organizadorService.getOrganizadores();
    }

    @Operation(summary = "Crear torneo", description = "El Organizador crea un nuevo torneo con nombre, fechas, cantidad de equipos y costo de inscripción.")
    @PostMapping("/organizadores/{id}/torneo")
    public Torneo crearTorneo(@PathVariable Long id, @RequestBody Torneo torneo) {
        return organizadorService.crearTorneo(id, torneo);
    }

    @Operation(summary = "Iniciar torneo", description = "El Organizador cambia el estado del torneo a EN_CURSO, habilitando el registro de partidos.")
    @PatchMapping("/organizadores/{id}/torneo/iniciar")
    public Torneo iniciarTorneo(@PathVariable Long id) {
        return organizadorService.iniciarTorneo(id);
    }

    @Operation(summary = "Finalizar torneo", description = "El Organizador cierra el torneo y lo marca como FINALIZADO.")
    @PatchMapping("/organizadores/{id}/torneo/finalizar")
    public Torneo finalizarTorneo(@PathVariable Long id) {
        return organizadorService.finalizarTorneo(id);
    }

    @Operation(summary = "Ver pagos pendientes", description = "El Organizador consulta todos los comprobantes de pago que aún no han sido verificados.")
    @GetMapping("/organizadores/{id}/pagos/pendientes")
    public List<Pago> pagosPendientes(@PathVariable Long id) {
        return pagoService.consultarPagosPendientes();
    }

    @Operation(summary = "Aprobar pago", description = "El Organizador aprueba el comprobante de pago de un equipo, confirmando su inscripción.")
    @PutMapping("/organizadores/{id}/pagos/{pagoId}/aprobar")
    public Pago aprobarPago(@PathVariable Long id, @PathVariable Long pagoId) {
        return pagoService.aprobarPago(pagoId);
    }

    @Operation(summary = "Configurar torneo", description = "El Organizador define el reglamento, canchas, horarios, sanciones y fecha de cierre de inscripciones del torneo.")
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

    @Operation(summary = "Rechazar pago", description = "El Organizador rechaza el comprobante de pago de un equipo por ser inválido o incompleto.")
    @PutMapping("/organizadores/{id}/pagos/{pagoId}/rechazar")
    public Pago rechazarPago(@PathVariable Long id, @PathVariable Long pagoId) {
        return pagoService.rechazarPago(pagoId);
    }

    @Operation(summary = "Crear partido", description = "El Organizador programa un partido entre dos equipos dentro de un torneo.")
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
}
