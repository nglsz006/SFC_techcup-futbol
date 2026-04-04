package edu.dosw.project.SFC_TechUp_Futbol.core.service;

import edu.dosw.project.SFC_TechUp_Futbol.controller.dto.request.ConsultaAuditoriaRequest;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.RegistroAuditoria;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.TipoAccionAuditoria;
import edu.dosw.project.SFC_TechUp_Futbol.core.util.IdGeneratorUtil;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.mapper.RegistroAuditoriaMapper;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.repository.RegistroAuditoriaJpaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
public class AuditoriaService {

    private final RegistroAuditoriaJpaRepository registroAuditoriaRepository;
    private final RegistroAuditoriaMapper mapper;

    public AuditoriaService(RegistroAuditoriaJpaRepository registroAuditoriaRepository, RegistroAuditoriaMapper mapper) {
        this.registroAuditoriaRepository = registroAuditoriaRepository;
        this.mapper = mapper;
    }

    public RegistroAuditoria registrarEvento(String administradorId, String usuario, TipoAccionAuditoria tipoAccion,
                                             String descripcion) {
        RegistroAuditoria registro = new RegistroAuditoria(
                IdGeneratorUtil.generarId(), administradorId, usuario, tipoAccion, descripcion, LocalDateTime.now());
        return mapper.toDomain(registroAuditoriaRepository.save(mapper.toEntity(registro)));
    }

    public List<RegistroAuditoria> consultarHistorial(ConsultaAuditoriaRequest request) {
        return registroAuditoriaRepository.findAll().stream()
                .map(mapper::toDomain)
                .filter(r -> coincideUsuario(r, request.getUsuario()))
                .filter(r -> coincideTipoAccion(r, request.getTipoAccion()))
                .filter(r -> coincideFechaDesde(r, request.getFechaDesde()))
                .filter(r -> coincideFechaHasta(r, request.getFechaHasta()))
                .sorted(Comparator.comparing(RegistroAuditoria::getFecha).reversed())
                .toList();
    }

    private boolean coincideUsuario(RegistroAuditoria r, String usuario) {
        if (usuario == null || usuario.isBlank()) return true;
        return r.getUsuario() != null && r.getUsuario().toLowerCase().contains(usuario.trim().toLowerCase());
    }

    private boolean coincideTipoAccion(RegistroAuditoria r, String tipoAccion) {
        if (tipoAccion == null || tipoAccion.isBlank()) return true;
        return r.getTipoAccion().name().equalsIgnoreCase(tipoAccion.trim());
    }

    private boolean coincideFechaDesde(RegistroAuditoria r, java.time.LocalDate fechaDesde) {
        if (fechaDesde == null) return true;
        return !r.getFecha().toLocalDate().isBefore(fechaDesde);
    }

    private boolean coincideFechaHasta(RegistroAuditoria r, java.time.LocalDate fechaHasta) {
        if (fechaHasta == null) return true;
        return !r.getFecha().toLocalDate().isAfter(fechaHasta);
    }
}
