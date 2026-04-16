package edu.dosw.project.SFC_TechUp_Futbol.core.service;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Invitacion;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.Jugador;
import edu.dosw.project.SFC_TechUp_Futbol.core.util.IdGeneratorUtil;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.mapper.InvitacionMapper;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.mapper.JugadorMapper;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.repository.InvitacionJpaRepository;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.repository.JugadorJpaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class InvitacionService {

    private final InvitacionJpaRepository invitacionRepository;
    private final InvitacionMapper invitacionMapper;
    private final JugadorJpaRepository jugadorRepository;
    private final JugadorMapper jugadorMapper;

    public InvitacionService(InvitacionJpaRepository invitacionRepository,
                             InvitacionMapper invitacionMapper,
                             JugadorJpaRepository jugadorRepository,
                             JugadorMapper jugadorMapper) {
        this.invitacionRepository = invitacionRepository;
        this.invitacionMapper = invitacionMapper;
        this.jugadorRepository = jugadorRepository;
        this.jugadorMapper = jugadorMapper;
    }

    public Invitacion enviar(String jugadorId, String equipoId, Jugador.Posicion posicion) {
        Jugador jugador = jugadorRepository.findById(jugadorId)
                .map(jugadorMapper::toDomain)
                .orElseThrow(() -> new IllegalArgumentException("Jugador no encontrado."));
        if (!jugador.isAvailable())
            throw new IllegalStateException("El jugador no está disponible.");

        Invitacion invitacion = new Invitacion();
        invitacion.setId(IdGeneratorUtil.generarId());
        invitacion.setJugadorId(jugadorId);
        invitacion.setEquipoId(equipoId);
        invitacion.setPosicion(posicion);
        invitacion.setFecha(LocalDateTime.now());
        invitacion.setEstado(Invitacion.EstadoInvitacion.PENDIENTE);
        return invitacionMapper.toDomain(invitacionRepository.save(invitacionMapper.toEntity(invitacion)));
    }

    public List<Invitacion> listarPorJugador(String jugadorId) {
        return invitacionRepository.findByJugadorId(jugadorId)
                .stream().map(invitacionMapper::toDomain).toList();
    }

    public List<Invitacion> listarPorEquipo(String equipoId) {
        return invitacionRepository.findByEquipoId(equipoId)
                .stream().map(invitacionMapper::toDomain).toList();
    }

    public Invitacion aceptar(String invitacionId) {
        Invitacion invitacion = getOrThrow(invitacionId);
        if (invitacion.getEstado() != Invitacion.EstadoInvitacion.PENDIENTE)
            throw new IllegalStateException("La invitación ya fue procesada.");

        jugadorRepository.findById(invitacion.getJugadorId()).ifPresent(j -> {
            j.setAvailable(false);
            j.setEquipoId(invitacion.getEquipoId());
            jugadorRepository.save(j);
        });

        invitacion.setEstado(Invitacion.EstadoInvitacion.ACEPTADA);
        return invitacionMapper.toDomain(invitacionRepository.save(invitacionMapper.toEntity(invitacion)));
    }

    public Invitacion rechazar(String invitacionId) {
        Invitacion invitacion = getOrThrow(invitacionId);
        if (invitacion.getEstado() != Invitacion.EstadoInvitacion.PENDIENTE)
            throw new IllegalStateException("La invitación ya fue procesada.");

        invitacion.setEstado(Invitacion.EstadoInvitacion.RECHAZADA);
        return invitacionMapper.toDomain(invitacionRepository.save(invitacionMapper.toEntity(invitacion)));
    }

    private Invitacion getOrThrow(String id) {
        return invitacionRepository.findById(id)
                .map(invitacionMapper::toDomain)
                .orElseThrow(() -> new IllegalArgumentException("Invitación no encontrada."));
    }
}
