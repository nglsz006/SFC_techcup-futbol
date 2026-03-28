package edu.dosw.project.SFC_TechUp_Futbol;

import edu.dosw.project.SFC_TechUp_Futbol.controller.dto.request.RegistroAdministrativoRequest;
import edu.dosw.project.SFC_TechUp_Futbol.core.exception.CorreoYaRegistradoException;
import edu.dosw.project.SFC_TechUp_Futbol.core.exception.RolNoPermitidoException;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.Organizador;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.Usuario;
import edu.dosw.project.SFC_TechUp_Futbol.core.repository.AdministradorRepository;
import edu.dosw.project.SFC_TechUp_Futbol.core.repository.ArbitroRepository;
import edu.dosw.project.SFC_TechUp_Futbol.core.repository.OrganizadorRepository;
import edu.dosw.project.SFC_TechUp_Futbol.core.repository.UsuarioRegistradoRepository;
import edu.dosw.project.SFC_TechUp_Futbol.core.validator.AdministradorValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class AdministradorValidatorTest {

    private AdministradorValidator administradorValidator;
    private OrganizadorRepository organizadorRepository;

    @BeforeEach
    void setUp() {
        Map<Long, Organizador> orgStore = new HashMap<>();
        AtomicLong orgIdGen = new AtomicLong(1);
        organizadorRepository = mock(OrganizadorRepository.class);
        when(organizadorRepository.save(any())).thenAnswer(inv -> {
            Organizador o = inv.getArgument(0);
            if (o.getId() == null) o.setId(orgIdGen.getAndIncrement());
            orgStore.put(o.getId(), o);
            return o;
        });
        when(organizadorRepository.findByEmail(anyString())).thenAnswer(inv -> {
            String email = inv.getArgument(0);
            return orgStore.values().stream().filter(o -> email.equals(o.getEmail())).findFirst();
        });
        when(organizadorRepository.findAll()).thenAnswer(inv -> new ArrayList<>(orgStore.values()));

        AdministradorRepository adminRepo = mock(AdministradorRepository.class);
        when(adminRepo.findByEmail(anyString())).thenReturn(Optional.empty());
        when(adminRepo.findAll()).thenReturn(new ArrayList<>());

        ArbitroRepository arbitroRepo = mock(ArbitroRepository.class);
        when(arbitroRepo.findByEmail(anyString())).thenReturn(Optional.empty());
        when(arbitroRepo.findAll()).thenReturn(new ArrayList<>());

        UsuarioRegistradoRepository usuarioRepo = mock(UsuarioRegistradoRepository.class);
        when(usuarioRepo.findByEmail(anyString())).thenReturn(Optional.empty());
        when(usuarioRepo.findAll()).thenReturn(new ArrayList<>());

        administradorValidator = new AdministradorValidator(adminRepo, organizadorRepository, arbitroRepo, usuarioRepo);
    }

    @Test
    void validarRegistro_cuandoEsValido_noLanzaExcepcion() {
        assertDoesNotThrow(() -> administradorValidator.validarRegistro(crearRequest("Organizador", "organizador@escuelaing.edu.co", "ORGANIZADOR")));
    }

    @Test
    void validarRegistro_cuandoNombreEsVacio_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> administradorValidator.validarRegistro(crearRequest(" ", "organizador@escuelaing.edu.co", "ORGANIZADOR")));
    }

    @Test
    void validarRegistro_cuandoCorreoYaExiste_lanzaExcepcion() {
        organizadorRepository.save(new Organizador(null, "Existente", "duplicado@escuelaing.edu.co", "password123", Usuario.TipoUsuario.ESTUDIANTE, null));
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
