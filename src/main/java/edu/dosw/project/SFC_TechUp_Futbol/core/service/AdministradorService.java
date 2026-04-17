package edu.dosw.project.SFC_TechUp_Futbol.core.service;

import edu.dosw.project.SFC_TechUp_Futbol.controller.dto.request.RegistroAdministrativoRequest;
import edu.dosw.project.SFC_TechUp_Futbol.core.exception.AccesoDenegadoException;
import edu.dosw.project.SFC_TechUp_Futbol.core.exception.CorreoYaRegistradoException;
import edu.dosw.project.SFC_TechUp_Futbol.core.exception.RolNoPermitidoException;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.*;
import edu.dosw.project.SFC_TechUp_Futbol.core.util.Base64Util;
import edu.dosw.project.SFC_TechUp_Futbol.core.util.IdGeneratorUtil;
import edu.dosw.project.SFC_TechUp_Futbol.core.util.PasswordUtil;
import edu.dosw.project.SFC_TechUp_Futbol.core.validator.AdministradorValidator;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.mapper.AdministradorMapper;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.mapper.ArbitroMapper;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.mapper.OrganizadorMapper;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.mapper.UsuarioRegistradoMapper;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.repository.AdministradorJpaRepository;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.repository.ArbitroJpaRepository;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.repository.OrganizadorJpaRepository;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.repository.UsuarioRegistradoJpaRepository;
import org.springframework.stereotype.Service;

@Service
public class AdministradorService {

    private final AdministradorJpaRepository administradorRepository;
    private final AdministradorMapper administradorMapper;
    private final OrganizadorJpaRepository organizadorRepository;
    private final OrganizadorMapper organizadorMapper;
    private final ArbitroJpaRepository arbitroRepository;
    private final ArbitroMapper arbitroMapper;
    private final UsuarioRegistradoJpaRepository usuarioRegistradoRepository;
    private final AuditoriaService auditoriaService;
    private final AdministradorValidator administradorValidator;

    public AdministradorService(AdministradorJpaRepository administradorRepository,
                                AdministradorMapper administradorMapper,
                                OrganizadorJpaRepository organizadorRepository,
                                OrganizadorMapper organizadorMapper,
                                ArbitroJpaRepository arbitroRepository,
                                ArbitroMapper arbitroMapper,
                                UsuarioRegistradoJpaRepository usuarioRegistradoRepository,
                                AuditoriaService auditoriaService) {
        this.administradorRepository = administradorRepository;
        this.administradorMapper = administradorMapper;
        this.organizadorRepository = organizadorRepository;
        this.organizadorMapper = organizadorMapper;
        this.arbitroRepository = arbitroRepository;
        this.arbitroMapper = arbitroMapper;
        this.usuarioRegistradoRepository = usuarioRegistradoRepository;
        this.auditoriaService = auditoriaService;
        this.administradorValidator = new AdministradorValidator(
                administradorRepository, organizadorRepository, arbitroRepository, usuarioRegistradoRepository);
    }

    public Administrador registrarAdministrador(RegistroAdministrativoRequest request) {
        validarCorreoUnico(request.getEmail());
        Administrador administrador = new Administrador(
                IdGeneratorUtil.generarId(), request.getNombre(), request.getEmail(),
                PasswordUtil.cifrar(request.getPassword()), request.getTipoUsuario(), true);
        return administradorMapper.toDomain(administradorRepository.save(administradorMapper.toEntity(administrador)));
    }

    public Usuario registrarUsuarioAdministrativo(String administradorId, RegistroAdministrativoRequest request) {
        administradorValidator.validarAdministradorId(administradorId);
        administradorValidator.validarRegistro(request);
        String rol = request.getRol() == null ? "" : request.getRol().trim().toUpperCase();
        return switch (rol) {
            case "ORGANIZADOR" -> registrarOrganizador(administradorId, request);
            case "ARBITRO" -> registrarArbitro(administradorId, request);
            default -> throw new RolNoPermitidoException("Solo se permite registrar usuarios con rol ORGANIZADOR o ARBITRO.");
        };
    }

    private Organizador registrarOrganizador(String administradorId, RegistroAdministrativoRequest request) {
        obtenerAdministradorAutorizado(administradorId);
        validarCorreoUnico(request.getEmail());
        Organizador organizador = new Organizador(
                IdGeneratorUtil.generarId(), request.getNombre(), request.getEmail(),
                PasswordUtil.cifrar(request.getPassword()), request.getTipoUsuario(), null);
        Organizador guardado = organizadorMapper.toDomain(organizadorRepository.save(organizadorMapper.toEntity(organizador)));
        auditoriaService.registrarEvento(administradorId, guardado.getEmail(),
                TipoAccionAuditoria.REGISTRO_ORGANIZADOR, "Registro administrativo de organizador.");
        return guardado;
    }

    private Arbitro registrarArbitro(String administradorId, RegistroAdministrativoRequest request) {
        obtenerAdministradorAutorizado(administradorId);
        validarCorreoUnico(request.getEmail());
        Arbitro arbitro = new Arbitro(
                IdGeneratorUtil.generarId(), request.getNombre(), request.getEmail(),
                PasswordUtil.cifrar(request.getPassword()), request.getTipoUsuario());
        Arbitro guardado = arbitroMapper.toDomain(arbitroRepository.save(arbitroMapper.toEntity(arbitro)));
        auditoriaService.registrarEvento(administradorId, guardado.getEmail(),
                TipoAccionAuditoria.REGISTRO_ARBITRO, "Registro administrativo de arbitro.");
        return guardado;
    }

    public Administrador obtenerAdministradorPorEmail(String email) {
        return administradorRepository.findByEmail(Base64Util.encode(email))
                .map(administradorMapper::toDomain)
                .orElseThrow(() -> new AccesoDenegadoException("El administrador no esta autorizado."));
    }

    private Administrador obtenerAdministradorAutorizado(String administradorId) {
        Administrador administrador = administradorRepository.findById(administradorId)
                .map(administradorMapper::toDomain)
                .orElseThrow(() -> new AccesoDenegadoException("El administrador no esta autorizado."));
        if (!administrador.isActivo()) throw new AccesoDenegadoException("El administrador no esta autorizado.");
        return administrador;
    }

    private void validarCorreoUnico(String email) {
        String emailEncoded = Base64Util.encode(email);
        if (administradorRepository.findByEmail(emailEncoded).isPresent()
                || organizadorRepository.findByEmail(emailEncoded).isPresent()
                || arbitroRepository.findByEmail(emailEncoded).isPresent()
                || usuarioRegistradoRepository.findByEmail(emailEncoded).isPresent()) {
            throw new CorreoYaRegistradoException("Ya existe un usuario con ese correo.");
        }
    }
}
