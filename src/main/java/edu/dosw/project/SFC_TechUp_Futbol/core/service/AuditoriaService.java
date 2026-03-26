package edu.dosw.project.SFC_TechUp_Futbol.core.service;

import edu.dosw.project.SFC_TechUp_Futbol.controller.dto.request.ConsultaAuditoriaRequest;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.RegistroAuditoria;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.TipoAccionAuditoria;
import edu.dosw.project.SFC_TechUp_Futbol.core.repository.RegistroAuditoriaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
public class AuditoriaService {

    private final RegistroAuditoriaRepository registroAuditoriaRepository;

    public AuditoriaService(RegistroAuditoriaRepository registroAuditoriaRepository) {
        this.registroAuditoriaRepository = registroAuditoriaRepository;
    }

    public RegistroAuditoria registrarEvento(Long administradorId, String usuario, TipoAccionAuditoria tipoAccion,
                                             String descripcion) {
        RegistroAuditoria registro = new RegistroAuditoria(
                null,
                administradorId,
                usuario,
                tipoAccion,
                descripcion,
                LocalDateTime.now()
        );
        return registroAuditoriaRepository.save(registro);
    }

    public List<RegistroAuditoria> consultarHistorial(ConsultaAuditoriaRequest request) {
        return registroAuditoriaRepository.findAll().stream()
                .filter(registro -> coincideUsuario(registro, request.getUsuario()))
                .filter(registro -> coincideTipoAccion(registro, request.getTipoAccion()))
                .filter(registro -> coincideFechaDesde(registro, request.getFechaDesde()))
                .filter(registro -> coincideFechaHasta(registro, request.getFechaHasta()))
                .sorted(Comparator.comparing(RegistroAuditoria::getFecha).reversed())
                .toList();
    }

    private boolean coincideUsuario(RegistroAuditoria registro, String usuario) {
        if (usuario == null || usuario.isBlank()) {
            return true;
        }
        return registro.getUsuario() != null
                && registro.getUsuario().toLowerCase().contains(usuario.trim().toLowerCase());
    }

    private boolean coincideTipoAccion(RegistroAuditoria registro, String tipoAccion) {
        if (tipoAccion == null || tipoAccion.isBlank()) {
            return true;
        }
        return registro.getTipoAccion().name().equalsIgnoreCase(tipoAccion.trim());
    }

    private boolean coincideFechaDesde(RegistroAuditoria registro, java.time.LocalDate fechaDesde) {
        if (fechaDesde == null) {
            return true;
        }
        return !registro.getFecha().toLocalDate().isBefore(fechaDesde);
    }

    private boolean coincideFechaHasta(RegistroAuditoria registro, java.time.LocalDate fechaHasta) {
        if (fechaHasta == null) {
            return true;
        }
        return !registro.getFecha().toLocalDate().isAfter(fechaHasta);
    }
}
