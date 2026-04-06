package edu.dosw.project.SFC_TechUp_Futbol.core.service;

import edu.dosw.project.SFC_TechUp_Futbol.core.exception.AutenticacionAdminException;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.Administrador;
import edu.dosw.project.SFC_TechUp_Futbol.core.util.Base64Util;
import edu.dosw.project.SFC_TechUp_Futbol.core.util.PasswordUtil;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.mapper.AdministradorMapper;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.repository.AdministradorJpaRepository;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AutenticacionAdministradorService {

    private final AdministradorJpaRepository administradorRepository;
    private final AdministradorMapper mapper;
    private final Map<String, String> sesiones = new ConcurrentHashMap<>();

    public AutenticacionAdministradorService(AdministradorJpaRepository administradorRepository, AdministradorMapper mapper) {
        this.administradorRepository = administradorRepository;
        this.mapper = mapper;
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
}
