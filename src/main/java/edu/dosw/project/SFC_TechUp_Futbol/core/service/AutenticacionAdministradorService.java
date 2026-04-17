package edu.dosw.project.SFC_TechUp_Futbol.core.service;

import edu.dosw.project.SFC_TechUp_Futbol.core.exception.AutenticacionAdminException;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.Administrador;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.TipoAccionAuditoria;
import edu.dosw.project.SFC_TechUp_Futbol.core.util.Base64Util;
import edu.dosw.project.SFC_TechUp_Futbol.core.util.PasswordUtil;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.mapper.AdministradorMapper;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.mapper.RegistroAuditoriaMapper;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.repository.AdministradorJpaRepository;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.repository.RegistroAuditoriaJpaRepository;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AutenticacionAdministradorService {

    private final AdministradorJpaRepository administradorRepository;
    private final AdministradorMapper mapper;
    private final AuditoriaService auditoriaService;
    private final Map<String, String> sesiones = new ConcurrentHashMap<>();

    public AutenticacionAdministradorService(AdministradorJpaRepository administradorRepository,
                                             AdministradorMapper mapper,
                                             RegistroAuditoriaJpaRepository registroAuditoriaRepository,
                                             RegistroAuditoriaMapper registroAuditoriaMapper) {
        this.administradorRepository = administradorRepository;
        this.mapper = mapper;
        this.auditoriaService = new AuditoriaService(registroAuditoriaRepository, registroAuditoriaMapper);
    }

    public String login(String email, String password) {
        Administrador administrador = administradorRepository.findByEmail(Base64Util.encode(email))
                .map(mapper::toDomain)
                .orElseThrow(() -> new AutenticacionAdminException("Credenciales de administrador incorrectas."));

        if (!administrador.isActivo() || !PasswordUtil.verificar(password, administrador.getPassword())) {
            throw new AutenticacionAdminException("Credenciales de administrador incorrectas.");
        }

        String token = UUID.randomUUID().toString();
        sesiones.put(token, administrador.getId());
        auditoriaService.registrarEvento(administrador.getId(), administrador.getEmail(),
                TipoAccionAuditoria.LOGIN_ADMIN, "Inicio de sesion administrativo.");
        return token;
    }

    public void validarSesion(String administradorId, String token) {
        if (administradorId == null || token == null || token.isBlank()) {
            throw new AutenticacionAdminException("La autenticacion del administrador es obligatoria.");
        }

        String administradorAutenticado = sesiones.get(token);
        if (administradorAutenticado == null || !administradorAutenticado.equals(administradorId)) {
            throw new AutenticacionAdminException("La sesion del administrador no es valida.");
        }
    }

    public String getAdministradorId(String token) {
        return sesiones.get(token);
    }
}
