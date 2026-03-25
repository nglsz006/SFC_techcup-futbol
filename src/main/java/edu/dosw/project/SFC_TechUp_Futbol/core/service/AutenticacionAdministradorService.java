package edu.dosw.project.SFC_TechUp_Futbol.core.service;

import edu.dosw.project.SFC_TechUp_Futbol.core.exception.AutenticacionAdminException;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.Administrador;
import edu.dosw.project.SFC_TechUp_Futbol.core.repository.AdministradorRepository;
import edu.dosw.project.SFC_TechUp_Futbol.core.util.PasswordUtil;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AutenticacionAdministradorService {

    private final AdministradorRepository administradorRepository;
    private final Map<String, Long> sesiones = new ConcurrentHashMap<>();

    public AutenticacionAdministradorService(AdministradorRepository administradorRepository) {
        this.administradorRepository = administradorRepository;
    }

    public String login(String email, String password) {
        Administrador administrador = administradorRepository.findByEmail(email)
                .orElseThrow(() -> new AutenticacionAdminException("Credenciales de administrador incorrectas."));

        if (!administrador.isActivo() || !PasswordUtil.verificar(password, administrador.getPassword())) {
            throw new AutenticacionAdminException("Credenciales de administrador incorrectas.");
        }

        String token = UUID.randomUUID().toString();
        sesiones.put(token, administrador.getId());
        return token;
    }

    public void validarSesion(Long administradorId, String token) {
        if (administradorId == null || token == null || token.isBlank()) {
            throw new AutenticacionAdminException("La autenticacion del administrador es obligatoria.");
        }

        Long administradorAutenticado = sesiones.get(token);
        if (administradorAutenticado == null || !administradorAutenticado.equals(administradorId)) {
            throw new AutenticacionAdminException("La sesion del administrador no es valida.");
        }
    }
}
