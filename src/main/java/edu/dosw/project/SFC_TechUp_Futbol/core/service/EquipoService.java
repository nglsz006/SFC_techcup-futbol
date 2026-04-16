package edu.dosw.project.SFC_TechUp_Futbol.core.service;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Equipo;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.Partido;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.Usuario;
import edu.dosw.project.SFC_TechUp_Futbol.core.util.IdGeneratorUtil;
import edu.dosw.project.SFC_TechUp_Futbol.core.validator.ValidacionEquipo;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.entity.JugadorEntity;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.mapper.EquipoMapper;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.repository.EquipoJpaRepository;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.repository.JugadorJpaRepository;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.Torneo;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.repository.PartidoJpaRepository;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.repository.TorneoJpaRepository;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@org.springframework.stereotype.Service
public class EquipoService extends Subject {

    private static final Logger log = Logger.getLogger(EquipoService.class.getName());

    private static String sanitize(String input) {
        return input == null ? "null" : input.replaceAll("[\r\n\t]", "_");
    }

    private final EquipoJpaRepository repository;
    private final EquipoMapper mapper;
    @org.springframework.beans.factory.annotation.Autowired(required = false)
    private PartidoJpaRepository partidoRepository;
    @org.springframework.beans.factory.annotation.Autowired(required = false)
    private JugadorJpaRepository jugadorRepository;
    @org.springframework.beans.factory.annotation.Autowired(required = false)
    private TorneoJpaRepository torneoRepository;

    public EquipoService(EquipoJpaRepository repository, EquipoMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public Equipo crear(Equipo equipo, Map<String, Object> datos) {
        new ValidacionEquipo().validar(datos);
        if (equipo.getId() == null) equipo.setId(IdGeneratorUtil.generarId());
        Equipo saved = mapper.toDomain(repository.save(mapper.toEntity(equipo)));
        log.info("Equipo creado con id: " + sanitize(saved.getId()));
        notificar("EQUIPO_CREADO", Map.of("id", saved.getId(), "nombre", saved.getNombre()));
        return saved;
    }

    public Equipo actualizar(String id, Equipo datos) {
        Equipo equipo = obtener(id);
        if (datos.getNombre() != null && !datos.getNombre().isBlank()) equipo.setNombre(datos.getNombre());
        if (datos.getEscudo() != null) equipo.setEscudo(datos.getEscudo());
        if (datos.getColorPrincipal() != null) equipo.setColorPrincipal(datos.getColorPrincipal());
        if (datos.getColorSecundario() != null) equipo.setColorSecundario(datos.getColorSecundario());
        return mapper.toDomain(repository.save(mapper.toEntity(equipo)));
    }

    public Equipo obtener(String id) {
        return repository.findById(id)
            .map(mapper::toDomain)
            .orElseThrow(() -> new IllegalArgumentException("Equipo no encontrado"));
    }

    public List<Equipo> listar() {
        return repository.findAll().stream().map(mapper::toDomain).toList();
    }

    public Equipo agregarJugador(String equipoId, String jugadorId) {
        if (torneoRepository != null) {
            boolean torneoEnCurso = !torneoRepository.findByEstado(Torneo.EstadoTorneo.EN_CURSO).isEmpty();
            if (torneoEnCurso)
                throw new IllegalStateException("No se pueden realizar cambios de jugadores mientras el torneo está en curso.");
        }
        Equipo equipo = obtener(equipoId);
        equipo.agregarJugador(jugadorId);
        log.info("Jugador agregado al equipo");
        notificar("JUGADOR_AGREGADO", Map.of("equipoId", equipoId, "jugadorId", jugadorId));
        return mapper.toDomain(repository.save(mapper.toEntity(equipo)));
    }

    public void eliminar(String id) {
        obtener(id);
        if (partidoRepository != null) {
            boolean tienePartidosActivos = partidoRepository
                .findByEquipoLocalIdOrEquipoVisitanteId(id, id).stream()
                .anyMatch(p -> p.getEstado() == Partido.PartidoEstado.PROGRAMADO
                           || p.getEstado() == Partido.PartidoEstado.EN_CURSO);
            if (tienePartidosActivos)
                throw new IllegalStateException("No se puede eliminar un equipo con partidos programados o en curso.");
        }
        repository.deleteById(id);
        log.info("Equipo eliminado con id: " + sanitize(id));
    }

    public Map<String, Object> validarComposicion(String equipoId) {
        Equipo equipo = obtener(equipoId);
        int total = equipo.getJugadores().size();
        boolean valido = total >= 8 && total <= 12;
        Map<String, Object> resultado = new java.util.LinkedHashMap<>();
        resultado.put("equipoId", equipoId);
        resultado.put("nombre", equipo.getNombre());
        resultado.put("totalJugadores", total);
        resultado.put("valido", valido);
        if (total < 8) resultado.put("error", "El equipo necesita al menos " + (8 - total) + " jugador(es) más.");
        if (total > 12) resultado.put("error", "El equipo excede el máximo de 12 jugadores.");

        if (jugadorRepository != null && total > 0) {
            List<JugadorEntity> jugadores = equipo.getJugadores().stream()
                    .map(id -> jugadorRepository.findById(id).orElse(null))
                    .filter(j -> j != null)
                    .toList();

            // Validar que todos sean de tipos permitidos
            List<Usuario.TipoUsuario> tiposPermitidos = List.of(
                    Usuario.TipoUsuario.ESTUDIANTE, Usuario.TipoUsuario.PROFESOR,
                    Usuario.TipoUsuario.PERSONAL_ADMIN, Usuario.TipoUsuario.GRADUADO,
                    Usuario.TipoUsuario.FAMILIAR);
            boolean todosPermitidos = jugadores.stream()
                    .allMatch(j -> j.getUserType() != null && tiposPermitidos.contains(j.getUserType()));
            if (!todosPermitidos) {
                resultado.put("errorTipoUsuario", "Todos los miembros deben ser estudiantes, profesores, personal administrativo, graduados o familiares.");
                resultado.put("valido", false);
            }

            // Validar que más de la mitad sean de carreras prioritarias
            List<Usuario.Carrera> carrerasPrioritarias = List.of(
                    Usuario.Carrera.INGENIERIA_SISTEMAS, Usuario.Carrera.IA,
                    Usuario.Carrera.CIBERSEGURIDAD, Usuario.Carrera.ESTADISTICA);
            long mitadPrioritaria = jugadores.stream()
                    .filter(j -> j.getCarrera() != null && carrerasPrioritarias.contains(j.getCarrera()))
                    .count();
            if (total > 0 && mitadPrioritaria <= total / 2) {
                resultado.put("errorCarrera", "Más de la mitad de los miembros deben ser de Ingeniería de Sistemas, IA, Ciberseguridad o Estadística.");
                resultado.put("valido", false);
            }
        }

        return resultado;
    }
}
