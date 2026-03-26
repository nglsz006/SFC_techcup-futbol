package edu.dosw.project.SFC_TechUp_Futbol;

import edu.dosw.project.SFC_TechUp_Futbol.controller.dto.request.RegistroAdministrativoRequest;
import edu.dosw.project.SFC_TechUp_Futbol.core.exception.AccesoDenegadoException;
import edu.dosw.project.SFC_TechUp_Futbol.core.exception.CorreoYaRegistradoException;
import edu.dosw.project.SFC_TechUp_Futbol.core.exception.RolNoPermitidoException;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.Administrador;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.Arbitro;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.Organizador;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.TipoAccionAuditoria;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.Usuario;
import edu.dosw.project.SFC_TechUp_Futbol.core.service.AdministradorService;
import edu.dosw.project.SFC_TechUp_Futbol.core.service.AuditoriaService;
import edu.dosw.project.SFC_TechUp_Futbol.core.util.PasswordUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AdministradorServiceTest {

    private AdministradorService administradorService;
    private RegistroAuditoriaRepositoryImpl auditoriaRepository;

    @BeforeEach
    void setUp() {
        auditoriaRepository = new RegistroAuditoriaRepositoryImpl();
        administradorService = new AdministradorService(
                new AdministradorRepositoryImpl(),
                new OrganizadorRepositoryImpl(),
                new ArbitroRepositoryImpl(),
                new UsuarioRegistradoRepositoryImpl(),
                new AuditoriaService(auditoriaRepository)
        );
    }

    @Test
    void registrarUsuarioAdministrativo_conRolOrganizador_registraOrganizador() {
        Administrador administrador = administradorService.registrarAdministrador(crearRequest("Admin", "admin@escuelaing.edu.co", "ORGANIZADOR"));

        Usuario usuario = administradorService.registrarUsuarioAdministrativo(
                administrador.getId(),
                crearRequest("Org", "org@escuelaing.edu.co", "ORGANIZADOR")
        );

        assertInstanceOf(Organizador.class, usuario);
        assertEquals("org@escuelaing.edu.co", usuario.getEmail());
        assertTrue(PasswordUtil.verificar("password123", usuario.getPassword()));
        assertEquals(TipoAccionAuditoria.REGISTRO_ORGANIZADOR, auditoriaRepository.findAll().getFirst().getTipoAccion());
    }

    @Test
    void registrarUsuarioAdministrativo_conRolArbitro_registraArbitro() {
        Administrador administrador = administradorService.registrarAdministrador(crearRequest("Admin", "admin@escuelaing.edu.co", "ORGANIZADOR"));

        Usuario usuario = administradorService.registrarUsuarioAdministrativo(
                administrador.getId(),
                crearRequest("Arbitro", "arbitro@escuelaing.edu.co", "ARBITRO")
        );

        assertInstanceOf(Arbitro.class, usuario);
        assertEquals("arbitro@escuelaing.edu.co", usuario.getEmail());
        assertEquals(TipoAccionAuditoria.REGISTRO_ARBITRO, auditoriaRepository.findAll().getFirst().getTipoAccion());
    }

    @Test
    void registrarUsuarioAdministrativo_sinAdministradorAutorizado_lanzaExcepcion() {
        assertThrows(AccesoDenegadoException.class,
                () -> administradorService.registrarUsuarioAdministrativo(99L, crearRequest("Org", "org@escuelaing.edu.co", "ORGANIZADOR")));
    }

    @Test
    void registrarUsuarioAdministrativo_conCorreoDuplicado_lanzaExcepcion() {
        Administrador administrador = administradorService.registrarAdministrador(crearRequest("Admin", "admin@escuelaing.edu.co", "ORGANIZADOR"));
        administradorService.registrarUsuarioAdministrativo(administrador.getId(), crearRequest("Org", "duplicado@escuelaing.edu.co", "ORGANIZADOR"));

        assertThrows(CorreoYaRegistradoException.class,
                () -> administradorService.registrarUsuarioAdministrativo(administrador.getId(), crearRequest("Arb", "duplicado@escuelaing.edu.co", "ARBITRO")));
    }

    @Test
    void registrarUsuarioAdministrativo_conRolNoPermitido_lanzaExcepcion() {
        Administrador administrador = administradorService.registrarAdministrador(crearRequest("Admin", "admin@escuelaing.edu.co", "ORGANIZADOR"));

        assertThrows(RolNoPermitidoException.class,
                () -> administradorService.registrarUsuarioAdministrativo(administrador.getId(), crearRequest("Supervisor", "sup@escuelaing.edu.co", "SUPERVISOR")));
    }

    private RegistroAdministrativoRequest crearRequest(String nombre, String email, String rol) {
        RegistroAdministrativoRequest request = new RegistroAdministrativoRequest();
        request.setNombre(nombre);
        request.setEmail(email);
        request.setPassword("password123");
        request.setTipoUsuario(Usuario.TipoUsuario.ESTUDIANTE);
        request.setRol(rol);
        return request;
    }
}
