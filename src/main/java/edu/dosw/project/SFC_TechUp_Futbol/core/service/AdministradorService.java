package edu.dosw.project.SFC_TechUp_Futbol.core.service;

import edu.dosw.project.SFC_TechUp_Futbol.controller.dto.request.RegistroAdministrativoRequest;
import edu.dosw.project.SFC_TechUp_Futbol.core.exception.AccesoDenegadoException;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.Administrador;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.Arbitro;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.Organizador;
import edu.dosw.project.SFC_TechUp_Futbol.core.repository.AdministradorRepository;
import edu.dosw.project.SFC_TechUp_Futbol.core.repository.ArbitroRepository;
import edu.dosw.project.SFC_TechUp_Futbol.core.repository.OrganizadorRepository;
import edu.dosw.project.SFC_TechUp_Futbol.core.repository.UsuarioRegistradoRepository;
import edu.dosw.project.SFC_TechUp_Futbol.core.util.PasswordUtil;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdministradorService {

    private final AdministradorRepository administradorRepository;
    private final OrganizadorRepository organizadorRepository;
    private final ArbitroRepository arbitroRepository;
    private final UsuarioRegistradoRepository usuarioRegistradoRepository;

    public AdministradorService(AdministradorRepository administradorRepository,
                                OrganizadorRepository organizadorRepository,
                                ArbitroRepository arbitroRepository,
                                UsuarioRegistradoRepository usuarioRegistradoRepository) {
        this.administradorRepository = administradorRepository;
        this.organizadorRepository = organizadorRepository;
        this.arbitroRepository = arbitroRepository;
        this.usuarioRegistradoRepository = usuarioRegistradoRepository;
    }

    public Administrador registrarAdministrador(RegistroAdministrativoRequest request) {
        validarCorreoUnico(request.getEmail());
        Administrador administrador = new Administrador(
                null,
                request.getNombre(),
                request.getEmail(),
                PasswordUtil.cifrar(request.getPassword()),
                request.getTipoUsuario(),
                true
        );
        return administradorRepository.save(administrador);
    }

    public Organizador registrarOrganizador(Long administradorId, RegistroAdministrativoRequest request) {
        obtenerAdministradorAutorizado(administradorId);
        validarCorreoUnico(request.getEmail());
        Organizador organizador = new Organizador(
                null,
                request.getNombre(),
                request.getEmail(),
                PasswordUtil.cifrar(request.getPassword()),
                request.getTipoUsuario(),
                null
        );
        return organizadorRepository.save(organizador);
    }

    public Arbitro registrarArbitro(Long administradorId, RegistroAdministrativoRequest request) {
        obtenerAdministradorAutorizado(administradorId);
        validarCorreoUnico(request.getEmail());
        Arbitro arbitro = new Arbitro(
                null,
                request.getNombre(),
                request.getEmail(),
                PasswordUtil.cifrar(request.getPassword()),
                request.getTipoUsuario()
        );
        return arbitroRepository.save(arbitro);
    }

    public List<Administrador> listarAdministradores() {
        return administradorRepository.findAll();
    }

    private Administrador obtenerAdministradorAutorizado(Long administradorId) {
        Administrador administrador = administradorRepository.findById(administradorId)
                .orElseThrow(() -> new AccesoDenegadoException("El administrador no esta autorizado para realizar esta operacion."));
        if (!administrador.isActivo()) {
            throw new AccesoDenegadoException("El administrador no esta autorizado para realizar esta operacion.");
        }
        return administrador;
    }

    private void validarCorreoUnico(String email) {
        if (administradorRepository.findByEmail(email).isPresent()
                || organizadorRepository.findByEmail(email).isPresent()
                || arbitroRepository.findByEmail(email).isPresent()
                || usuarioRegistradoRepository.findByEmail(email).isPresent()) {
            throw new IllegalStateException("Ya existe un usuario con ese correo.");
        }
    }
}
