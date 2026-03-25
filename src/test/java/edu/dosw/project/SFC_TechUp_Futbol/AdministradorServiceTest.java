package edu.dosw.project.SFC_TechUp_Futbol;

import edu.dosw.project.SFC_TechUp_Futbol.controller.dto.request.RegistroAdministrativoRequest;
import edu.dosw.project.SFC_TechUp_Futbol.core.exception.AccesoDenegadoException;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.Administrador;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.Arbitro;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.Organizador;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.Usuario;
import edu.dosw.project.SFC_TechUp_Futbol.core.repository.AdministradorRepositoryImpl;
import edu.dosw.project.SFC_TechUp_Futbol.core.repository.ArbitroRepositoryImpl;
import edu.dosw.project.SFC_TechUp_Futbol.core.repository.OrganizadorRepositoryImpl;
import edu.dosw.project.SFC_TechUp_Futbol.core.repository.UsuarioRegistradoRepositoryImpl;
import edu.dosw.project.SFC_TechUp_Futbol.core.service.AdministradorService;
import edu.dosw.project.SFC_TechUp_Futbol.core.util.PasswordUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AdministradorServiceTest {

    private AdministradorService administradorService;

    @BeforeEach
    void setUp() {
        administradorService = new AdministradorService(
                new AdministradorRepositoryImpl(),
                new OrganizadorRepositoryImpl(),
                new ArbitroRepositoryImpl(),
                new UsuarioRegistradoRepositoryImpl()
        );
    }

    @Test
    void registrarAdministrador_guardaPasswordCifrada() {
        Administrador administrador = administradorService.registrarAdministrador(crearRequest("Admin", "admin@escuelaing.edu.co"));

        assertNotNull(administrador.getId());
        assertTrue(PasswordUtil.verificar("password123", administrador.getPassword()));
    }

    @Test
    void registrarOrganizador_conAdministradorActivo_retornaOrganizador() {
        Administrador administrador = administradorService.registrarAdministrador(crearRequest("Admin", "admin@escuelaing.edu.co"));

        Organizador organizador = administradorService.registrarOrganizador(administrador.getId(), crearRequest("Org", "org@escuelaing.edu.co"));

        assertNotNull(organizador.getId());
        assertEquals("org@escuelaing.edu.co", organizador.getEmail());
    }

    @Test
    void registrarArbitro_sinAdministradorAutorizado_lanzaExcepcion() {
        assertThrows(AccesoDenegadoException.class,
                () -> administradorService.registrarArbitro(99L, crearRequest("Arb", "arb@escuelaing.edu.co")));
    }

    @Test
    void registrarArbitro_conCorreoDuplicado_lanzaExcepcion() {
        Administrador administrador = administradorService.registrarAdministrador(crearRequest("Admin", "admin@escuelaing.edu.co"));
        administradorService.registrarOrganizador(administrador.getId(), crearRequest("Org", "duplicado@escuelaing.edu.co"));

        assertThrows(IllegalStateException.class,
                () -> administradorService.registrarArbitro(administrador.getId(), crearRequest("Arb", "duplicado@escuelaing.edu.co")));
    }

    private RegistroAdministrativoRequest crearRequest(String nombre, String email) {
        RegistroAdministrativoRequest request = new RegistroAdministrativoRequest();
        request.setNombre(nombre);
        request.setEmail(email);
        request.setPassword("password123");
        request.setTipoUsuario(Usuario.TipoUsuario.ESTUDIANTE);
        return request;
    }
}
