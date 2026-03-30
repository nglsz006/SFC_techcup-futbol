package edu.dosw.project.SFC_TechUp_Futbol.core.service;

import edu.dosw.project.SFC_TechUp_Futbol.controller.dto.request.RegistroAdministrativoRequest;
import edu.dosw.project.SFC_TechUp_Futbol.core.exception.AccesoDenegadoException;
import edu.dosw.project.SFC_TechUp_Futbol.core.exception.CorreoYaRegistradoException;
import edu.dosw.project.SFC_TechUp_Futbol.core.exception.RolNoPermitidoException;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.*;
import edu.dosw.project.SFC_TechUp_Futbol.core.repository.*;
import edu.dosw.project.SFC_TechUp_Futbol.core.util.PasswordUtil;
import org.springframework.stereotype.Service;

@Service
public class AdministradorService {

    private final AdministradorRepository administradorRepository;
    private final OrganizadorRepository organizadorRepository;
    private final ArbitroRepository arbitroRepository;
    private final UsuarioRegistradoRepository usuarioRegistradoRepository;
    private final AuditoriaService auditoriaService;

    public AdministradorService(AdministradorRepository administradorRepository,
                                OrganizadorRepository organizadorRepository,
                                ArbitroRepository arbitroRepository,
                                UsuarioRegistradoRepository usuarioRegistradoRepository,
                                AuditoriaService auditoriaService) {
        this.administradorRepository = administradorRepository;
        this.organizadorRepository = organizadorRepository;
        this.arbitroRepository = arbitroRepository;
        this.usuarioRegistradoRepository = usuarioRegistradoRepository;
        this.auditoriaService = auditoriaService;
    }

    public Administrador registrarAdministrador(RegistroAdministrativoRequest request) {
        validarCorreoUnico(request.getEmail());
        Administrador administrador = new Administrador(
                null, request.getNombre(), request.getEmail(),
                PasswordUtil.cifrar(request.getPassword()), request.getTipoUsuario(), true);
        return administradorRepository.save(administrador);
    }

    public Usuario registrarUsuarioAdministrativo(String administradorId, RegistroAdministrativoRequest request) {
        String rol = request.getRol() == null ? "" : request.getRol().trim().toUpperCase();
        return switch (rol) {
            case "ORGANIZADOR" -> registrarOrganizador(administradorId, request);
            case "ARBITRO" -> registrarArbitro(administradorId, request);
            default -> throw new RolNoPermitidoException("Solo se permite registrar usuarios con rol ORGANIZADOR o ARBITRO.");
        };
    }

    public Organizador registrarOrganizador(String administradorId, RegistroAdministrativoRequest request) {
        obtenerAdministradorAutorizado(administradorId);
        validarCorreoUnico(request.getEmail());
        Organizador organizador = new Organizador(
                null, request.getNombre(), request.getEmail(),
                PasswordUtil.cifrar(request.getPassword()), request.getTipoUsuario(), null);
        Organizador guardado = organizadorRepository.save(organizador);
        auditoriaService.registrarEvento(administradorId, guardado.getEmail(),
                TipoAccionAuditoria.REGISTRO_ORGANIZADOR, "Registro administrativo de organizador.");
        return guardado;
    }

    public Arbitro registrarArbitro(String administradorId, RegistroAdministrativoRequest request) {
        obtenerAdministradorAutorizado(administradorId);
        validarCorreoUnico(request.getEmail());
        Arbitro arbitro = new Arbitro(
                null, request.getNombre(), request.getEmail(),
                PasswordUtil.cifrar(request.getPassword()), request.getTipoUsuario());
        Arbitro guardado = arbitroRepository.save(arbitro);
        auditoriaService.registrarEvento(administradorId, guardado.getEmail(),
                TipoAccionAuditoria.REGISTRO_ARBITRO, "Registro administrativo de arbitro.");
        return guardado;
    }

    public Administrador obtenerAdministradorPorEmail(String email) {
        return administradorRepository.findByEmail(email)
                .orElseThrow(() -> new AccesoDenegadoException("El administrador no esta autorizado."));
    }

    private Administrador obtenerAdministradorAutorizado(String administradorId) {
        Administrador administrador = administradorRepository.findById(administradorId)
                .orElseThrow(() -> new AccesoDenegadoException("El administrador no esta autorizado."));
        if (!administrador.isActivo()) throw new AccesoDenegadoException("El administrador no esta autorizado.");
        return administrador;
    }

    private void validarCorreoUnico(String email) {
        if (administradorRepository.findByEmail(email).isPresent()
                || organizadorRepository.findByEmail(email).isPresent()
                || arbitroRepository.findByEmail(email).isPresent()
                || usuarioRegistradoRepository.findByEmail(email).isPresent()) {
            throw new CorreoYaRegistradoException("Ya existe un usuario con ese correo.");
        }
    }
}
