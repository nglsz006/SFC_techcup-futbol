package edu.dosw.project.SFC_TechUp_Futbol;

import edu.dosw.project.SFC_TechUp_Futbol.controller.dto.request.RegistroAdministrativoRequest;
import edu.dosw.project.SFC_TechUp_Futbol.core.exception.CorreoYaRegistradoException;
import edu.dosw.project.SFC_TechUp_Futbol.core.exception.RolNoPermitidoException;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.Usuario;
import edu.dosw.project.SFC_TechUp_Futbol.core.repository.AdministradorRepositoryImpl;
import edu.dosw.project.SFC_TechUp_Futbol.core.repository.ArbitroRepositoryImpl;
import edu.dosw.project.SFC_TechUp_Futbol.core.repository.OrganizadorRepositoryImpl;
import edu.dosw.project.SFC_TechUp_Futbol.core.repository.UsuarioRegistradoRepositoryImpl;
import edu.dosw.project.SFC_TechUp_Futbol.core.validator.AdministradorValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AdministradorValidatorTest {

    private AdministradorValidator administradorValidator;
    private OrganizadorRepositoryImpl organizadorRepository;

    @BeforeEach
    void setUp() {
        organizadorRepository = new OrganizadorRepositoryImpl();
        administradorValidator = new AdministradorValidator(
                new AdministradorRepositoryImpl(),
                organizadorRepository,
                new ArbitroRepositoryImpl(),
                new UsuarioRegistradoRepositoryImpl()
        );
    }

    @Test
    void validarRegistro_cuandoEsValido_noLanzaExcepcion() {
        assertDoesNotThrow(() -> administradorValidator.validarRegistro(crearRequest("Organizador", "organizador@escuelaing.edu.co", "ORGANIZADOR")));
    }

    @Test
    void validarRegistro_cuandoNombreEsVacio_lanzaExcepcion() {
        RegistroAdministrativoRequest request = crearRequest(" ", "organizador@escuelaing.edu.co", "ORGANIZADOR");

        assertThrows(IllegalArgumentException.class, () -> administradorValidator.validarRegistro(request));
    }

    @Test
    void validarRegistro_cuandoCorreoYaExiste_lanzaExcepcion() {
        organizadorRepository.save(new edu.dosw.project.SFC_TechUp_Futbol.core.model.Organizador(
                null,
                "Existente",
                "duplicado@escuelaing.edu.co",
                "password123",
                Usuario.TipoUsuario.ESTUDIANTE,
                null
        ));

        assertThrows(CorreoYaRegistradoException.class,
                () -> administradorValidator.validarRegistro(crearRequest("Nuevo", "duplicado@escuelaing.edu.co", "ORGANIZADOR")));
    }

    @Test
    void validarRegistro_cuandoRolNoEsPermitido_lanzaExcepcion() {
        assertThrows(RolNoPermitidoException.class,
                () -> administradorValidator.validarRegistro(crearRequest("Supervisor", "supervisor@escuelaing.edu.co", "SUPERVISOR")));
    }

    @Test
    void validarRegistro_cuandoSolicitudEsNula_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> administradorValidator.validarRegistro(null));
    }

    @Test
    void validarRegistro_cuandoRolEsVacio_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class,
                () -> administradorValidator.validarRegistro(crearRequest("Organizador", "org2@escuelaing.edu.co", " ")));
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
